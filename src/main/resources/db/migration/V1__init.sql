-- =============================================================================
-- V1__init.sql  –  Schema (Create Tables)
-- Tương thích với H2 Database (in-memory & file-based)
-- =============================================================================

-- ── Users ─────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP,
    UNIQUE (tenant_id, email)
);
CREATE INDEX idx_users_tenant_email ON users (tenant_id, email);

-- ── Product Type ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product_type (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    syntax      VARCHAR(255) NOT NULL,
    description TEXT,
    enabled     BOOLEAN NOT NULL DEFAULT TRUE
);

-- ── Product Category (new) ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product_category (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    syntax          VARCHAR(255) NOT NULL,
    description     TEXT,
    product_type_id BIGINT NOT NULL,
    enabled         BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (product_type_id) REFERENCES product_type(id)
);
CREATE INDEX idx_product_category_type ON product_category(product_type_id);

-- ── Brand ─────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS brand (
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(255) NOT NULL,
    logo             VARCHAR(255),
    long_description TEXT,
    enabled          BOOLEAN NOT NULL DEFAULT TRUE
);

-- ── Product ───────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    syntax              VARCHAR(255),
    description         TEXT,
    price               NUMERIC(19, 2),
    is_sale_off         BOOLEAN NOT NULL DEFAULT FALSE,
    sale_percent        INT     DEFAULT 0,
    images              TEXT,
    enabled             BOOLEAN NOT NULL DEFAULT TRUE,
    product_category_id BIGINT  NOT NULL,
    FOREIGN KEY (product_category_id) REFERENCES product_category(id)
);
CREATE INDEX idx_product_category ON product(product_category_id);

-- ── Product Detail ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS product_detail (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id          BIGINT  NOT NULL,
    is_vat              BOOLEAN NOT NULL DEFAULT FALSE,
    in_stock            BOOLEAN NOT NULL DEFAULT TRUE,
    short_description   TEXT,
    summary_description TEXT,
    detail_description  TEXT,
    final_description   TEXT,
    technical_functions TEXT,
    brand_id            BIGINT,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE SET NULL
);
CREATE INDEX idx_product_detail_product ON product_detail(product_id);
CREATE INDEX idx_product_detail_brand ON product_detail(brand_id);

-- ── Promotion ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS promotion (
    id                BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_detail_id BIGINT       NOT NULL,
    title             VARCHAR(255) NOT NULL,
    description       TEXT,
    start_date        DATE         NOT NULL,
    end_date          DATE         NOT NULL,
    enabled           BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (product_detail_id) REFERENCES product_detail(id) ON DELETE CASCADE
);
CREATE INDEX idx_promotion_detail ON promotion(product_detail_id);

