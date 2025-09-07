-- ----------------------------
-- Table structure for expedition
-- ----------------------------
DROP TABLE IF EXISTS "expedition" CASCADE;
CREATE TABLE "expedition" (
  "id" BIGSERIAL NOT NULL,
  "expedition_name" varchar(100) COLLATE "pg_catalog"."default",
  "price" numeric(10,2),
  "created_by"  BIGINT       NOT NULL    DEFAULT 0,
  "created_at"  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  "updated_by"  BIGINT       NULL,
  "updated_at"  TIMESTAMP WITH TIME ZONE,
  "deleted_by"  BIGINT       NULL,
  "deleted_at"  TIMESTAMP WITH TIME ZONE
)
;

-- ----------------------------
-- Primary Key structure for table expedition
-- ----------------------------
ALTER TABLE "expedition" ADD CONSTRAINT "expedition_pkey" PRIMARY KEY ("id");
