-- ----------------------------
-- Table structure for black_list_token
-- ----------------------------
DROP TABLE IF EXISTS "black_list_token" CASCADE;
CREATE TABLE "black_list_token" (
  "id" BIGSERIAL NOT NULL,
  "token" text COLLATE "pg_catalog"."default" NOT NULL,
  "created_by"  BIGINT       NOT NULL    DEFAULT 0,
  "created_at"  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  "updated_by"  BIGINT       NULL,
  "updated_at"  TIMESTAMP WITH TIME ZONE,
  "deleted_by"  BIGINT       NULL,
  "deleted_at"  TIMESTAMP WITH TIME ZONE
)
;

-- ----------------------------
-- Primary Key structure for table black_list_token
-- ----------------------------
ALTER TABLE "black_list_token" ADD CONSTRAINT "black_list_token_pkey" PRIMARY KEY ("id");
