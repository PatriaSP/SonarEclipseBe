-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS "roles" CASCADE;
CREATE TABLE "roles" (
  "id" BIGSERIAL NOT NULL,
  "role_name" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Primary Key structure for table roles
-- ----------------------------
ALTER TABLE "roles" ADD CONSTRAINT "roles_pkey" PRIMARY KEY ("id");
