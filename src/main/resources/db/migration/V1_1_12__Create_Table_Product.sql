-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS "product" CASCADE;
CREATE TABLE "product" (
  "id" BIGSERIAL NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "category_id" int4,
  "price" numeric(10,2),
  "image" varchar(255) COLLATE "pg_catalog"."default",
  "stock" int4,
  "created_by"  BIGINT       NOT NULL    DEFAULT 0,
  "created_at"  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  "updated_by"  BIGINT       NULL,
  "updated_at"  TIMESTAMP WITH TIME ZONE,
  "deleted_by"  BIGINT       NULL,
  "deleted_at"  TIMESTAMP WITH TIME ZONE,
  "is_active" bool
)
;

-- ----------------------------
-- Primary Key structure for table product
-- ----------------------------
ALTER TABLE "product" ADD CONSTRAINT "product_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table product
-- ----------------------------
ALTER TABLE "product" ADD CONSTRAINT "fk_category" FOREIGN KEY ("category_id") REFERENCES "category" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
