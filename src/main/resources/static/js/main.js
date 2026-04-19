document.addEventListener('DOMContentLoaded', function () {

  // ── Mobile navigation toggle ──────────────────────────────────────────────
  var toggle = document.getElementById('navToggle');
  var menu   = document.getElementById('navMenu');

  if (toggle && menu) {
    toggle.addEventListener('click', function () {
      menu.classList.toggle('open');
      // Animate hamburger → X
      toggle.classList.toggle('is-open');
    });
    menu.querySelectorAll('a').forEach(function (link) {
      link.addEventListener('click', function () {
        menu.classList.remove('open');
        toggle.classList.remove('is-open');
      });
    });
    document.addEventListener('click', function (e) {
      if (!menu.contains(e.target) && !toggle.contains(e.target)) {
        menu.classList.remove('open');
        toggle.classList.remove('is-open');
      }
    });
  }

  // ── Search (debounced dropdown) ──────────────────────────────────────────
  var searchInput = document.getElementById('siteSearchInput');
  var searchBtn = document.getElementById('siteSearchBtn');
  var searchDropdown = document.getElementById('searchDropdown');
  var debounceTimer = null;
  var selectedIndex = -1; // Track keyboard selection

  // Client-side cache for dropdown queries (simple LRU-like with cap)
  var searchCache = new Map();
  var cacheOrder = [];
  var CACHE_LIMIT = 60;

  function cachePut(key, value) {
    if (searchCache.has(key)) {
      // refresh order
      var idx = cacheOrder.indexOf(key);
      if (idx !== -1) cacheOrder.splice(idx, 1);
    }
    cacheOrder.unshift(key);
    searchCache.set(key, value);
    if (cacheOrder.length > CACHE_LIMIT) {
      var old = cacheOrder.pop();
      searchCache.delete(old);
    }
  }

  function cacheGet(key) {
    if (!searchCache.has(key)) return null;
    // refresh order
    var idx = cacheOrder.indexOf(key);
    if (idx !== -1) {
      cacheOrder.splice(idx, 1);
      cacheOrder.unshift(key);
    }
    return searchCache.get(key);
  }

  // Score results for better accuracy (promote items with query at start of syntax, then contains in syntax, then description)
  function scoreItem(item, q) {
    if (!q) return 0;
    var s = 0;
    var ql = q.toLowerCase();
    if (item.syntax) {
      var t = String(item.syntax).toLowerCase();
      if (t === ql) s += 100;
      else if (t.startsWith(ql)) s += 60;
      else if (t.indexOf(ql) !== -1) s += 30;
    }
    if (item.description) {
      var d = String(item.description).toLowerCase();
      if (d.indexOf(ql) !== -1) s += 10;
    }
    // small boost if on sale
    if (item.saleOff) s += 5;
    return s;
  }

  // Highlight matched query in text (simple substring, case-insensitive)
  function highlightMatch(text, q) {
    if (!text || !q) return escapeHtml(text || '');
    try {
      var qi = q.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'); // escape regex
      var re = new RegExp('(' + qi + ')', 'ig');
      return escapeHtml(String(text)).replace(re, '<span class="search-highlight">$1</span>');
    } catch (e) { return escapeHtml(text); }
  }

  function renderDropdown(items, q) {
    if (!searchDropdown) return;
    if (!items || items.length === 0) {
      searchDropdown.style.display = 'none';
      searchDropdown.innerHTML = '';
      selectedIndex = -1;
      return;
    }

    // sort by score for better accuracy
    if (q) {
      items = items.slice();
      items.sort(function (a, b) {
        return scoreItem(b, q) - scoreItem(a, q);
      });
    }

    var html = '<div class="search-grid">';
    items.forEach(function (p) {
      var id = p.id;
      var title = p.syntax || p.description || (p.id ? ('Sản phẩm ' + p.id) : '');
      var desc = p.description || '';
      var type = p.productTypeName || '';
      var category = p.productCategoryName || '';
      var images = p.images || [];
      var price = p.price != null ? formatPrice(p.price) : '';
      var sale = p.salePrice != null ? formatPrice(p.salePrice) : '';
      var salePct = p.salePercent != null && p.salePercent > 0 ? p.salePercent + '%' : '';

      html += '<div class="search-item" data-id="' + id + '">';

      // small multi-thumb preview: show up to 3 thumbs, or a single main image
      html += '<div class="s-img">';
      if (images.length === 0) {
        html += '<div class="s-img-placeholder">📦</div>';
      } else if (images.length === 1) {
        html += '<img src="' + escapeHtml(images[0]) + '" alt="' + escapeHtml(title) + '"/>';
      } else {
        html += '<div class="s-thumbs">';
        for (var i = 0; i < Math.min(3, images.length); i++) {
          html += '<div class="s-thumb"><img src="' + escapeHtml(images[i]) + '" alt="' + escapeHtml(title) + '"/></div>';
        }
        html += '</div>';
      }
      html += '</div>';

      html += '<div class="s-info">';
      html += '<div class="s-top">';
      if (type) html += '<span class="product-card-type">' + escapeHtml(type) + '</span>';
      if (category) html += '<span class="product-card-type" style="background:var(--accent-light);color:var(--accent-dark);">' + escapeHtml(category) + '</span>';
      if (salePct) html += '<span class="badge-sale" style="margin-left:8px">-' + escapeHtml(salePct) + '</span>';
      html += '</div>';

      // highlight matches in title and description using q
      html += '<div class="s-title">' + highlightMatch(title, q) + '</div>';
      html += '<div class="s-desc">' + highlightMatch(shorten(desc, 120), q) + '</div>';

      html += '<div class="s-price-group">';
      if (sale) {
        html += '<div class="product-price-original">' + escapeHtml(price) + '</div>';
        html += '<div class="product-price-sale">' + escapeHtml(sale) + '</div>';
      } else {
        html += '<div class="product-price" style="font-weight:800">' + escapeHtml(price) + '</div>';
      }
      html += '</div>';

      html += '</div>'; // s-info
      html += '</div>'; // search-item
    });
    html += '</div>';

    searchDropdown.innerHTML = html;
    searchDropdown.style.display = 'block';
    selectedIndex = -1; // reset selection on new render

    // Attach click handlers
    searchDropdown.querySelectorAll('.search-item').forEach(function (el) {
      el.addEventListener('click', function () {
        var id = el.getAttribute('data-id');
        if (id) {
          window.location.href = '/product/' + id;
        }
      });
    });
  }

  function navigateDropdown(direction) {
    var items = searchDropdown.querySelectorAll('.search-item');
    if (items.length === 0) return;

    // Update selectedIndex
    if (direction === 'next') {
      selectedIndex = (selectedIndex + 1) % items.length;
    } else if (direction === 'prev') {
      selectedIndex = (selectedIndex - 1 + items.length) % items.length;
    }

    // Update UI: remove old highlight, add to new
    items.forEach(function (el, idx) {
      if (idx === selectedIndex) {
        el.classList.add('search-item-selected');
        // Scroll into view
        el.scrollIntoView({ block: 'nearest' });
      } else {
        el.classList.remove('search-item-selected');
      }
    });
  }

  function selectCurrentItem() {
    var items = searchDropdown.querySelectorAll('.search-item');
    if (selectedIndex >= 0 && selectedIndex < items.length) {
      var id = items[selectedIndex].getAttribute('data-id');
      if (id) {
        window.location.href = '/product/' + id;
      }
    }
  }

  function escapeHtml(text) {
    if (!text) return '';
    return String(text).replace(/[&<>"']/g, function (s) {
      return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"})[s];
    });
  }

  function doDropdownQuery(q) {
    var key = (q || '').trim().toLowerCase();
    if (!q || q.trim().length === 0) {
      renderDropdown([]);
      return;
    }

    // Check cache
    var cached = cacheGet(key);
    if (cached) {
      renderDropdown(cached, q);
      return;
    }

    fetch('/api/data/products/search/dropdown?q=' + encodeURIComponent(q.trim()))
      .then(function (res) { return res.json(); })
      .then(function (data) {
        // cache results
        cachePut(key, data);
        renderDropdown(data, q);
      })
      .catch(function (err) { console.error('Search dropdown error', err); renderDropdown([]); });
  }

  if (searchInput) {
    searchInput.addEventListener('input', function (e) {
      var v = e.target.value;
      clearTimeout(debounceTimer);
      debounceTimer = setTimeout(function () {
        doDropdownQuery(v);
      }, 1000); // 1s debounce
    });

    // Keyboard navigation
    searchInput.addEventListener('keydown', function (e) {
      if (!searchDropdown || searchDropdown.style.display === 'none') {
        // Dropdown not shown
        if (e.key === 'Enter') {
          e.preventDefault();
          var q = searchInput.value || '';
          if (q.trim().length > 0) {
            window.location.href = '/services?keyword=' + encodeURIComponent(q.trim()) + '&filtered=true';
          }
        }
        return;
      }

      var items = searchDropdown.querySelectorAll('.search-item');
      if (items.length === 0) return;

      if (e.key === 'ArrowDown') {
        e.preventDefault();
        navigateDropdown('next');
      } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        navigateDropdown('prev');
      } else if (e.key === 'Enter') {
        e.preventDefault();
        if (selectedIndex >= 0) {
          selectCurrentItem();
        } else {
          // No selection, treat as full search
          var q = searchInput.value || '';
          if (q.trim().length > 0) {
            window.location.href = '/services?keyword=' + encodeURIComponent(q.trim()) + '&filtered=true';
          }
        }
      } else if (e.key === 'Escape') {
        e.preventDefault();
        searchDropdown.style.display = 'none';
        selectedIndex = -1;
      }
    });

    // Hide dropdown when clicking outside
    document.addEventListener('click', function (e) {
      if (!searchDropdown.contains(e.target) && !searchInput.contains(e.target)) {
        if (searchDropdown) searchDropdown.style.display = 'none';
        selectedIndex = -1;
      }
    });
  }

  if (searchBtn) {
    searchBtn.addEventListener('click', function () {
      var q = searchInput.value || '';
      if (q.trim().length > 0) {
        window.location.href = '/services?keyword=' + encodeURIComponent(q.trim()) + '&filtered=true';
      }
    });
  }

  // ── Gallery image switcher ────────────────────────────────────────────────
  window.switchImg = function (thumb, src) {
    var mainImg = document.getElementById('mainImg');
    if (mainImg) mainImg.src = src;
    document.querySelectorAll('.gallery-thumb').forEach(function (t) {
      t.classList.remove('active');
    });
    thumb.classList.add('active');
  };

  // ── Scroll-reveal animation ───────────────────────────────────────────────
  var revealEls = document.querySelectorAll(
    '.card, .icon-card, .product-card, .testimonial-card, .service-mini-card, .hero-stat-card, .stat-item, .value-card, .contact-info-card, .team-card'
  );

  if ('IntersectionObserver' in window) {
    var io = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          entry.target.style.opacity = '1';
          entry.target.style.transform = 'translateY(0)';
          io.unobserve(entry.target);
        }
      });
    }, { threshold: 0.1, rootMargin: '0px 0px -30px 0px' });

    revealEls.forEach(function (el) {
      el.style.opacity = '0';
      el.style.transform = 'translateY(18px)';
      el.style.transition = 'opacity 0.45s ease, transform 0.45s ease';
      io.observe(el);
    });
  }

  // ── Smooth counter animation for stats ───────────────────────────────────
  var statNums = document.querySelectorAll('.stat-item .num');
  if ('IntersectionObserver' in window && statNums.length > 0) {
    var statsObserver = new IntersectionObserver(function (entries, obs) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          animateCount(entry.target);
          obs.unobserve(entry.target);
        }
      });
    }, { threshold: 0.5 });
    statNums.forEach(function (el) { statsObserver.observe(el); });
  }

  function animateCount(el) {
    var raw = el.textContent.trim();
    var num = parseFloat(raw.replace(/[^0-9.]/g, ''));
    if (isNaN(num) || num === 0) return;
    var suffix = raw.replace(/[0-9.,]/g, '');
    var duration = 1200;
    var steps = 40;
    var step = 0;
    var timer = setInterval(function () {
      step++;
      var progress = step / steps;
      var eased = 1 - Math.pow(1 - progress, 3);
      var current = num * eased;
      if (Number.isInteger(num)) {
        el.textContent = Math.floor(current).toLocaleString('vi-VN') + suffix;
      } else {
        el.textContent = current.toFixed(1) + suffix;
      }
      if (step >= steps) {
        clearInterval(timer);
        el.textContent = raw;
      }
    }, duration / steps);
  }

  function formatPrice(v) {
    try {
      // v may be number or string
      var n = typeof v === 'number' ? v : Number(v);
      if (isNaN(n)) return '';
      return n.toLocaleString('vi-VN') + '₫';
    } catch (e) { return String(v); }
  }

  function shorten(text, max) {
    if (!text) return '';
    if (text.length <= max) return text;
    return text.substring(0, max - 1).trim() + '…';
  }

  // ── Mega Menu ─────────────────────────────────────────────────────────────
  var servicesContent  = document.getElementById('servicesMenuContent');
  var headerMenuContent = document.getElementById('headerMenuContent');

  if (servicesContent || headerMenuContent) {
    fetch('/api/data/menu/product-types')
      .then(r => r.json())
      .then(data => {
        if (servicesContent)  renderMenuInto(servicesContent,  data);
        if (headerMenuContent) renderMenuInto(headerMenuContent, data);
      })
      .catch(e => console.error('Menu load failed:', e));
  }

  var typeIcons = {
    'Di động': '📋',
    'Truyền hình': '📋',
    'Internet': '📋',
    'Doanh nghiệp': '📋'
  };

  function getTypeIcon(typeName) {
    return '📋'; // Single icon for all types
  }

  function renderMenuInto(content, types) {
    if (!content) return;

    // 3 separate columns
    content.innerHTML =
      '<div class="services-column" id="menuCol1"></div>' +
      '<div class="services-column" id="menuCol2"></div>' +
      '<div class="services-column" id="menuCol3"></div>';

    var col1 = document.getElementById('menuCol1');
    var col2 = document.getElementById('menuCol2');
    var col3 = document.getElementById('menuCol3');

    // Populate column 1 with types
    types.forEach(function(type, typeIdx) {
      var el = document.createElement('div');
      el.className = 'services-item services-type-item';
      el.textContent = '📋 ' + type.syntax;
      el.setAttribute('data-type-id', type.id);

      // Hover: populate col2 with categories
      el.addEventListener('mouseenter', function() {
        // Highlight active type
        col1.querySelectorAll('.services-item').forEach(function(i) { i.classList.remove('active'); });
        el.classList.add('active');

        col2.innerHTML = '';
        col3.innerHTML = '';

        if (!type.categories || !type.categories.length) {
          col2.innerHTML = '<div class="services-item disabled">Không có danh mục</div>';
          return;
        }

        type.categories.forEach(function(cat) {
          var catEl = document.createElement('div');
          catEl.className = 'services-item services-cat-item';
          catEl.textContent = '📦 ' + cat.syntax;
          catEl.setAttribute('data-cat-id', cat.id);
          catEl.setAttribute('data-type-id', type.id);

          // Hover: populate col3 with brands
          catEl.addEventListener('mouseenter', function() {
            col2.querySelectorAll('.services-item').forEach(function(i) { i.classList.remove('active'); });
            catEl.classList.add('active');

            col3.innerHTML = '';

            if (!cat.brands || !cat.brands.length) {
              col3.innerHTML = '<div class="services-item disabled">Không có thương hiệu</div>';
              return;
            }

            cat.brands.forEach(function(brand) {
              var brandEl = document.createElement('div');
              brandEl.className = 'services-item services-brand-item';
              brandEl.textContent = '🔗 ' + brand.name;

              brandEl.addEventListener('click', function() {
                window.location.href = '/services?typeId=' + type.id + '&categoryId=' + cat.id + '&filtered=true';
              });

              col3.appendChild(brandEl);
            });
          });

          // Click category
          catEl.addEventListener('click', function() {
            window.location.href = '/services?typeId=' + type.id + '&categoryId=' + cat.id + '&filtered=true';
          });

          col2.appendChild(catEl);
        });
      });

      // Click type
      el.addEventListener('click', function() {
        window.location.href = '/services?typeId=' + type.id + '&filtered=true';
      });

      col1.appendChild(el);
    });
  }
});
