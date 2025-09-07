-- ----------------------------
-- Table structure for product_review
-- ----------------------------
DROP TABLE IF EXISTS "product_review" CASCADE;
CREATE TABLE "product_review" (
  "id" BIGSERIAL NOT NULL,
  "review" text COLLATE "pg_catalog"."default",
  "user_id" int4,
  "product_id" int4,
  "transactions_id" int4,
  "score" int2,
  "created_by"  BIGINT       NOT NULL    DEFAULT 0,
  "created_at"  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  "updated_by"  BIGINT       NULL,
  "updated_at"  TIMESTAMP WITH TIME ZONE,
  "deleted_by"  BIGINT       NULL,
  "deleted_at"  TIMESTAMP WITH TIME ZONE
)
;

-- ----------------------------
-- Primary Key structure for table product_review
-- ----------------------------
ALTER TABLE "product_review" ADD CONSTRAINT "product_review_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table product_review
-- ----------------------------
ALTER TABLE "product_review" ADD CONSTRAINT "fk_product_id" FOREIGN KEY ("product_id") REFERENCES "product" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "product_review" ADD CONSTRAINT "fk_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "basket" ADD CONSTRAINT "fk_product" FOREIGN KEY ("product_id") REFERENCES "product" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
