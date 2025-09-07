-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS "payment" CASCADE;
CREATE TABLE "payment" (
  "id" BIGSERIAL NOT NULL,
  "method" varchar(255) COLLATE "pg_catalog"."default",
  "created_by"  BIGINT       NOT NULL    DEFAULT 0,
  "created_at"  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  "updated_by"  BIGINT       NULL,
  "updated_at"  TIMESTAMP WITH TIME ZONE,
  "deleted_by"  BIGINT       NULL,
  "deleted_at"  TIMESTAMP WITH TIME ZONE
)
;

-- ----------------------------
-- Primary Key structure for table payment
-- ----------------------------
ALTER TABLE "payment" ADD CONSTRAINT "payment_pkey" PRIMARY KEY ("id");
