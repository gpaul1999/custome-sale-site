-- =============================================================================
-- V2__seed_demo_data.sql  –  Insert Demo Data
-- Tương thích với H2 và PostgreSQL
-- =============================================================================

-- ── Product Type ──────────────────────────────────────────────────────────────
INSERT INTO product_type (syntax, description, enabled) VALUES
    ('Di động',      'Điện thoại & SIM 4G/5G linh hoạt cho mọi nhu cầu',          TRUE),
    ('Truyền hình',  'Trải nghiệm giải trí đỉnh cao với hàng trăm kênh HD',        TRUE),
    ('Internet',     'Internet cáp quang tốc độ cao, ổn định 24/7',                TRUE),
    ('Doanh nghiệp', 'Giải pháp kết nối toàn diện cho doanh nghiệp',               TRUE);

-- ── Product Category ──────────────────────────────────────────────────────────
-- Di động (product_type_id = 1)
INSERT INTO product_category (syntax, description, product_type_id, enabled) VALUES
    ('Gói data',             'Các gói data 4G/5G với mức giá và dung lượng khác nhau',   1, TRUE),
    ('Gói ưu đãi',           'Gói khuyến mãi dành cho sinh viên, học sinh',               1, TRUE),
    ('Gói doanh nghiệp',     'Các gói dành cho doanh nghiệp, cơ quan',                    1, TRUE);

-- Truyền hình (product_type_id = 2)
INSERT INTO product_category (syntax, description, product_type_id, enabled) VALUES
    ('Gói cơ bản',           'Gói xem TV cơ bản với 100+ kênh HD',                        2, TRUE),
    ('Gói gia đình',         'Gói gia đình với 200+ kênh 4K, VOD không giới hạn',          2, TRUE),
    ('Gói premium',          'Gói cao cấp với 250+ kênh 4K/8K, thể thao quốc tế',          2, TRUE);

-- Internet (product_type_id = 3)
INSERT INTO product_category (syntax, description, product_type_id, enabled) VALUES
    ('Internet cơ bản',      'Internet tốc độ 50Mbps, phù hợp hộ gia đình nhỏ',           3, TRUE),
    ('Internet tiêu chuẩn',  'Internet 200Mbps, phù hợp gia đình 4-5 người',               3, TRUE),
    ('Internet cao cấp',     'Internet 1Gbps, phù hợp nhà thông minh, công ty',            3, TRUE);

-- Doanh nghiệp (product_type_id = 4)
INSERT INTO product_category (syntax, description, product_type_id, enabled) VALUES
    ('Kết nối doanh nghiệp', 'Leased Line, VPN, MPLS cho kết nối văn phòng',               4, TRUE),
    ('Dịch vụ cloud',        'Cloud hosting, lưu trữ đám mây, CDN',                        4, TRUE);

-- ── Brand ─────────────────────────────────────────────────────────────────────
INSERT INTO brand (name, logo, long_description, enabled) VALUES
    ('Viettel',  '/images/brand/viettel.png',  '["Mạng di động lớn nhất Việt Nam","Phủ sóng toàn quốc 63 tỉnh thành","Công nghệ 4G/5G tiên tiến"]',  TRUE),
    ('VNPT',     '/images/brand/vnpt.png',     '["Tập đoàn viễn thông quốc gia","Hạ tầng cáp quang rộng khắp","Dịch vụ internet & truyền hình số"]', TRUE),
    ('FPT',      '/images/brand/fpt.png',      '["Nhà cung cấp internet hàng đầu","Tốc độ cao, ổn định","Hỗ trợ kỹ thuật 24/7"]',                   TRUE),
    ('MobiFone', '/images/brand/mobifone.png', '["Mạng di động uy tín","Gói cước đa dạng linh hoạt","Chất lượng dịch vụ xuất sắc"]',                 TRUE);

-- ── Product ───────────────────────────────────────────────────────────────────
-- Di động - Gói data (category_id = 1)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('SIM 4G Viettel 7GB',        'Gói data 7GB tốc độ cao, gọi nội mạng miễn phí',          70000,  FALSE, 0,  '["/images/product/sim-viettel-7gb.png"]',    1, TRUE),
    ('SIM 4G Viettel 30GB',       'Gói data 30GB, gọi miễn phí nội & ngoại mạng',             150000, TRUE,  20, '["/images/product/sim-viettel-30gb.png"]',   1, TRUE),
    ('SIM 5G MobiFone Unlimited', 'Data không giới hạn, tốc độ 5G khi có vùng phủ',           250000, FALSE, 0,  '["/images/product/sim-mobi-unlimited.png"]', 1, TRUE);

-- Di động - Gói ưu đãi (category_id = 2)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('SIM Sinh viên 15GB',        'Ưu đãi đặc biệt cho sinh viên, 15GB/tháng',                90000,  TRUE,  15, '["/images/product/sim-sinhvien.png"]',       2, TRUE),
    ('SIM Học sinh 10GB',         'Ưu đãi cho học sinh, 10GB + 1000 phút gọi nội mạng',       60000,  FALSE, 0,  '["/images/product/sim-hocsinh.png"]',        2, TRUE);

-- Truyền hình - Gói cơ bản (category_id = 4)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('Truyền hình Cơ bản',        '100+ kênh HD, kho VOD 500 phim',                           80000,  FALSE, 0,  '["/images/product/tv-basic.png"]',           4, TRUE);

-- Truyền hình - Gói gia đình (category_id = 5)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('Truyền hình Gia đình',      '200+ kênh HD/4K, VOD không giới hạn, 3 thiết bị',          150000, FALSE, 0,  '["/images/product/tv-family.png"]',          5, TRUE);

-- Truyền hình - Gói premium (category_id = 6)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('Truyền hình Premium',       '250+ kênh HD/4K/8K, thể thao quốc tế, đa thiết bị',        250000, TRUE,  10, '["/images/product/tv-premium.png"]',         6, TRUE);

-- Internet - Cơ bản (category_id = 7)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('Internet 50Mbps',           'Tốc độ 50Mbps, không giới hạn data, modem miễn phí',       150000, FALSE, 0,  '["/images/product/inet-basic.png"]',         7, TRUE);

-- Internet - Tiêu chuẩn (category_id = 8)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('Internet 200Mbps',          'Tốc độ 200Mbps, router Wi-Fi 6, hỗ trợ 24/7',              250000, TRUE,  25, '["/images/product/inet-standard.png"]',      8, TRUE);

-- Internet - Cao cấp (category_id = 9)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('Internet 1Gbps',            'Tốc độ 1Gbps, Mesh Wi-Fi 6, SLA cam kết 99.9%',            450000, FALSE, 0,  '["/images/product/inet-premium.png"]',       9, TRUE);

-- Doanh nghiệp - Kết nối (category_id = 10)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('Leased Line 100Mbps',       'Đường truyền riêng đối xứng, SLA 99.99%, 24/7',            NULL,   FALSE, 0,  '["/images/product/biz-leasedline.png"]',    10, TRUE),
    ('VPN & MPLS',                'Kết nối văn phòng nhánh bảo mật, QoS ưu tiên',             NULL,   FALSE, 0,  '["/images/product/biz-vpn.png"]',           10, TRUE);

-- Doanh nghiệp - Cloud (category_id = 11)
INSERT INTO product (syntax, description, price, is_sale_off, sale_percent, images, product_category_id, enabled) VALUES
    ('Cloud Hosting',             'Server ảo, lưu trữ đám mây, CDN, data center Tier 3',      NULL,   FALSE, 0,  '["/images/product/biz-cloud.png"]',         11, TRUE);

-- ── Product Detail ────────────────────────────────────────────────────────────
-- product_id = 1: SIM 4G Viettel 7GB
INSERT INTO product_detail (product_id, is_vat, in_stock, short_description, summary_description, detail_description, final_description, technical_functions, brand_id) VALUES
    (1, FALSE, TRUE,
     '["7GB data tốc độ cao","Gọi nội mạng miễn phí","100 phút ngoại mạng"]',
     'Gói SIM phổ thông dành cho người dùng cơ bản.',
     '["Gói SIM 4G Viettel 7GB là lựa chọn tiết kiệm cho người dùng cơ bản.","Cung cấp 7GB data tốc độ cao mỗi tháng, phù hợp lướt web, mạng xã hội hàng ngày.","Gọi nội mạng Viettel miễn phí không giới hạn, tiết kiệm chi phí liên lạc.","Hỗ trợ công nghệ VoLTE giúp cuộc gọi rõ ràng hơn trên nền 4G."]',
     'Liên hệ hotline 18008168 để đăng ký ngay hôm nay.',
     '["Công nghệ: 4G LTE Cat.6 – Hỗ trợ tốc độ download tối đa 150Mbps","VoLTE: Cuộc gọi HD trên nền mạng 4G, chất lượng rõ ràng hơn 3G","VoWifi: Gọi qua Wi-Fi khi ở vùng sóng yếu","Vùng phủ: 95% dân số toàn quốc"]',
     1);

-- product_id = 2: SIM 4G Viettel 30GB
INSERT INTO product_detail (product_id, is_vat, in_stock, short_description, summary_description, detail_description, final_description, technical_functions, brand_id) VALUES
    (2, FALSE, TRUE,
     '["30GB data tốc độ cao","Gọi miễn phí nội & ngoại mạng","Roaming 10 quốc gia"]',
     'Gói SIM phổ biến nhất, phù hợp mọi đối tượng.',
     '["SIM 4G Viettel 30GB là gói cước bán chạy nhất với data dồi dào cho mọi nhu cầu.","30GB data tốc độ 4G/5G mỗi tháng, xem video HD, họp online mượt mà.","Gọi miễn phí nội mạng và ngoại mạng không giới hạn, không lo tốn cước.","Hỗ trợ Roaming tại 10 quốc gia châu Á, tiện lợi khi đi công tác hoặc du lịch."]',
     'Đăng ký qua app MyViettel hoặc USSD *098#.',
     '["Công nghệ: 4G/5G – Tốc độ download tối đa 500Mbps trên 5G","VoLTE: Gọi HD chất lượng cao trên nền 4G","Roaming quốc tế: Hỗ trợ 10 quốc gia khu vực châu Á","Data sau ngưỡng: Tốc độ giảm còn 64Kbps khi hết 30GB"]',
     1);

-- product_id = 8: Internet 200Mbps
INSERT INTO product_detail (product_id, is_vat, in_stock, short_description, summary_description, detail_description, final_description, technical_functions, brand_id) VALUES
    (8, TRUE, TRUE,
     '["Tốc độ 200Mbps","Router Wi-Fi 6 miễn phí","Lắp đặt miễn phí"]',
     'Gói internet gia đình được lựa chọn nhiều nhất.',
     '["Gói Internet 200Mbps của FPT mang đến trải nghiệm lướt web cực nhanh cho cả gia đình.","Tốc độ đối xứng 200Mbps cả upload lẫn download, phù hợp làm việc từ xa và giải trí.","Router Wi-Fi 6 được tặng kèm, hỗ trợ đến 64 thiết bị kết nối đồng thời.","Cam kết uptime 99.5% với đội ngũ kỹ thuật hỗ trợ tận nhà 24/7."]',
     'Hotline hỗ trợ kỹ thuật: 1800 599 900 (miễn phí).',
     '["Công nghệ: GPON Fiber – Cáp quang thế hệ mới, độ trễ thấp","Wi-Fi: Wi-Fi 6 (802.11ax) – Băng tần kép 2.4GHz + 5GHz","Thiết bị đồng thời: Hỗ trợ tối đa 64 thiết bị kết nối cùng lúc","IPv6: Hỗ trợ IPv4 + IPv6 dual stack","Uptime: Cam kết 99.5% SLA hàng tháng"]',
     3);

-- ── Promotion ─────────────────────────────────────────────────────────────────
-- product_detail_id = 1 (SIM 4G Viettel 7GB)
INSERT INTO promotion (product_detail_id, title, description, start_date, end_date, enabled) VALUES
    (1, 'Tặng 1GB tháng đầu',      'Đăng ký mới trong tháng 4/2026 được tặng thêm 1GB data sử dụng ngay tháng đầu tiên.',  '2026-04-01', '2026-04-30', TRUE),
    (1, 'Miễn phí SIM vật lý',     'Miễn phí SIM vật lý hoặc eSIM khi đăng ký online qua app MyViettel.',                   '2026-04-01', '2026-06-30', TRUE);

-- product_detail_id = 2 (SIM 4G Viettel 30GB)
INSERT INTO promotion (product_detail_id, title, description, start_date, end_date, enabled) VALUES
    (2, 'Giảm 20% tháng đầu',      'Áp dụng giảm 20% cước tháng đầu tiên cho khách hàng đăng ký mới trong tháng 4/2026.',  '2026-04-01', '2026-04-30', TRUE),
    (2, 'Roaming miễn phí 3 ngày', 'Tặng 3 ngày Roaming miễn phí tại Thái Lan, Campuchia hoặc Singapore.',                  '2026-03-01', '2026-05-31', TRUE);

-- product_detail_id = 3 (Internet 200Mbps)
INSERT INTO promotion (product_detail_id, title, description, start_date, end_date, enabled) VALUES
    (3, 'Tặng 2 tháng miễn phí',   'Đăng ký gói 1 năm được tặng thêm 2 tháng sử dụng miễn phí, tiết kiệm đến 500.000đ.',  '2026-04-01', '2026-05-31', TRUE),
    (3, 'Lắp đặt miễn phí',        'Miễn phí toàn bộ chi phí lắp đặt và thiết bị router Wi-Fi 6 cho khách hàng mới.',       '2026-01-01', '2026-12-31', TRUE);
