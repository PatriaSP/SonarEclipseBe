-- ----------------------------
-- Table structure for expedition_history
-- ----------------------------
DROP TABLE IF EXISTS "expedition_history" CASCADE;
CREATE TABLE "expedition_history" (
  "id" BIGSERIAL NOT NULL,
  "detail" text COLLATE "pg_catalog"."default",
  "created_by"  BIGINT       NOT NULL    DEFAULT 0,
  "created_at"  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  "updated_by"  BIGINT       NULL,
  "updated_at"  TIMESTAMP WITH TIME ZONE,
  "deleted_by"  BIGINT       NULL,
  "deleted_at"  TIMESTAMP WITH TIME ZONE,
  "expedition_id" int4,
  "transaction_id" int4
)
;

-- ----------------------------
-- Primary Key structure for table expedition_history
-- ----------------------------
ALTER TABLE "expedition_history" ADD CONSTRAINT "expedition_history_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table expedition_history
-- ----------------------------
ALTER TABLE "expedition_history" ADD CONSTRAINT "fk_expedition" FOREIGN KEY ("expedition_id") REFERENCES "expedition" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
