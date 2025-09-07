-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS "category" CASCADE;
CREATE TABLE "category" (
  "id" BIGSERIAL NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Primary Key structure for table category
-- ----------------------------
ALTER TABLE "category" ADD CONSTRAINT "category_pkey" PRIMARY KEY ("id");
