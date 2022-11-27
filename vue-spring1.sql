/*
 Navicat Premium Data Transfer

 Source Server         : 119.23.243.88
 Source Server Type    : MySQL
 Source Server Version : 50740
 Source Host           : 119.23.243.88:3306
 Source Schema         : vue-spring

 Target Server Type    : MySQL
 Target Server Version : 50740
 File Encoding         : 65001

 Date: 27/11/2022 13:05:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `creater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `othermessage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastedittime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of note
-- ----------------------------
INSERT INTO `note` VALUES (2, 'server', 'Arc2022', '# Docker\n## 1.  安装\n![1b15bf14319fe46e01.jpeg](http://119.23.243.88:8888/files/imgs/Arc2022/4fd040acbb9847cabd88cc96fd3901de.jpeg)\n## 选一个执行\n### docker执行完毕后\n\n> $ curl -fsSL get.docker.com -o get-docker.sh \n> #选一个执行\n> $ sudo sh get-docker.sh --mirror Aliyun\n>\n> 执行这个命令后，脚本就会自动的将一切准备工作做好，并且把 Docker 的稳定(stable)版本安装在系统中。\n>\n> #启动 Docker\n>\n> $ sudo systemctl enable docker\n> $ sudo systemctl start docker\n>\n> Client: Docker Engine - Community\n>  Version:           20.10.21\n>  API version:       1.41\n>  Go version:        go1.18.7\n>  Git commit:        baeda1f\n>  Built:             Tue Oct 25 18:02:28 2022\n>  OS/Arch:           linux/amd64\n>  Context:           default\n>  Experimental:      true\n>\n> Server: Docker Engine - Community\n>  Engine:\n>   Version:          20.10.21\n>   API version:      1.41 (minimum version 1.12)\n>   Go version:       go1.18.7\n>   Git commit:       3056208\n>   Built:            Tue Oct 25 18:00:19 2022\n>   OS/Arch:          linux/amd64\n>   Experimental:     false\n>  containerd:\n>   Version:          1.6.10\n>   GitCommit:        770bd0108c32f3fb5c73ae1264f7e503fe7b2661\n>  runc:\n>   Version:          1.1.4\n>   GitCommit:        v1.1.4-0-g5fd4c4d\n>  docker-init:\n>   Version:          0.19.0\n>   GitCommit:        de40ad0\n>\n> ================================================================================\n>\n> To run Docker as a non-privileged user, consider setting up the\n> Docker daemon in rootless mode for your user:\n>\n> ```dockerfile\n> dockerd-rootless-setuptool.sh install\n> ```\n>\n> Visit https://docs.docker.com/go/rootless/ to learn about rootless mode.\n>\n> To run the Docker daemon as a fully privileged service, but granting non-root\n> users access, refer to https://docs.docker.com/go/daemon-access/\n>\n> WARNING: Access to the remote API on a privileged Docker daemon is equivalent\n>          to root access on the host. Refer to the \'Docker daemon attack surface\'\n>          documentation for details: https://docs.docker.com/go/attack-surface/\n>\n> ================================================================================\n\n\n\n## 2.  重启Docker\n\n> systemctl daemon-reload\n>\n> service docker restart\n\n## 3. dockerfile\n\n```\nFROM openjdk:8\n\nVOLUME /tmp/vue-spring\n\nADD ./target/vuee-spring-0.0.1-SNAPSHOT.jar app.jar\n\nENTRYPOINT [\"java\" ,\"-Djava.security.egd=file:/dev/./urandom\",\"-jar\",\"/app.jar\"]\n```\n\n## 4. 部署mysql\n\n## 创建容器\n\n```\n\ndocker search mysql\n\ndocker pull mysql:5.7\n```\n\n```bash\ndocker run -id \\\n -p 3306:3306 \\\n --name=mysql \\\n -v $PWD/conf:/etc/mysql/conf.d \\\n -v $PWD/logs:/logs \\\n -v $PWD/data:/var/lib/mysql \\\n -e MYSQL_ROOT_PASSWORD=123456qwe \\\n mysql:5.7\n```\n\n\n\n```\ndocker exec -it mysql bash\n```\n\n在创建image的时候密码已经固定\n\n```\nALTER USER \'root\'@\'%\' IDENTIFIED WITH mysql_native_password BY \'password\';\n```\n\n```\nFLUSH PRIVILEGES;\n```\n\n## 5.部署vue\n\n```\nFROM node:14.16.0-alpine # 依赖的node环境需要根据库具体的本机node相匹配  alpine是线上专用\nMAINTAINER kezhou sun  # 作者信息\nENV NODE_ENV=production # 环境变量 生产环境\nENV HOST 0.0.0.0	# 可访问主机 所有ip\nRUN mkdir -p /app	# 执行命令创建app文件夹\nCOPY . /app 	# 拷贝所有文件到app文件夹下\nWORKDIR /app	# 设置工作目录\nEXPOSE 3000	# 暴露端口\n#If the environment in China build please open the following comments\n#RUN npm config set registry https://registry.npm.taobao.org\nRUN npm install	# 升级相关依赖包\nRUN npm run build	# 创建镜像\nCMD [\"npm\", \"start\"]	# 执行命令\n```\n## 6.## Alist\n```\nAlist 安装成功！\n\n访问地址：http://YOUR_IP:5244/\n\n配置文件路径：/opt/alist/data/config.json\n$查看管理员信息，请执行\ncd /opt/alist\n./alist admin\n\n查看状态：systemctl status alist\n启动服务：systemctl start alist\n重启服务：systemctl restart alist\n停止服务：systemctl stop alist\n\n温馨提示：如果端口无法正常访问，请检查 服务器安全组、本机防火墙、Alist状态\n\nroot@iZwz96ohhyvsmd59zwrgfqZ:~# cd /opt/alist\nroot@iZwz96ohhyvsmd59zwrgfqZ:/opt/alist# ./alist admin\nINFO[2022-11-23 01:23:12] reading config file: data/config.json\nINFO[2022-11-23 01:23:12] load config from env with prefix: ALIST_\nINFO[2022-11-23 01:23:12] init logrus...\nINFO[2022-11-23 01:23:12] admin user\'s info:\nusername: admin\npassword: TEW0MbqM\n```\n## 7. ## Caddy\n + Caddyfile\n```\n{\nhttp_port 8080\nauto_https off\n}\n:8080 127.0.0.1:8080{\n    root * /www\n    file_server\n}\n```\n+ Dockerfile\n```\n#FROM node:14.16.0-alpine as builder\n#ENV NODE_ENV=production\n#ENV HOST 0.0.0.0\n#RUN mkdir -p /app\n#COPY dist /app\n#WORKDIR /app\n#EXPOSE 8080\n\nFROM caddy\nVOLUME  /tmp/caddy\nWORKDIR /tmp/caddy\nCOPY dist /www\nCOPY docker/Caddyfile /etc/Caddyfile\nEXPOSE 8080\nEXPOSE 80\n\n#RUN npm config set registry https://registry.npm.taobao.org\n#CMD [\"npm\",\"run\",\"serve\"]\n\n```\n## 8.  Aira2\n+ aria2\n```\ndocker pull p3terx/aria2-pro:latest\n```\n\n ```\ndocker run -d \\\n--name aria2 \\\n--restart=always \\\n--log-opt max-size=100m \\\n-e PUID=$UID \\\n-e PGID=$GID \\\n-e UMASK_SET=022 \\\n-e RPC_SECRET=123456qwe \\\n-e RPC_PORT=6800 \\\n-e LISTEN_PORT=6888 \\\n-p 16800:6800 \\\n-p 16888:6888 \\\n-p 16888:6888/udp \\\n-v ~/aria2/config:/config \\\n-v ~/samba/downloads:/downloads \\\np3terx/aria2-pro:latest\n```\n+ aria2NG\n```\ndocker pull p3terx/ariang:latest\n```\n```\ndocker run -d \\\n--name AiraNg \\\n--log-opt max-size=100m \\\n--restart=always \\\n-p 16880:6880 \\\np3terx/ariang:latest\n```\n> 然后进aira2ng设置项-标签导航\n修改配置信息\n\n![1.jpg](http://119.23.243.88:8888/files/imgs/null)', NULL, '2022-11-06 13:24:28', '2022-11-26 18:57:07', 'springboot和mysql的docker部署');

-- ----------------------------
-- Table structure for thingstable
-- ----------------------------
DROP TABLE IF EXISTS `thingstable`;
CREATE TABLE `thingstable`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `type` int(10) NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `alertToken` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of thingstable
-- ----------------------------
INSERT INTO `thingstable` VALUES (32, 1, '思政实践', '2022-10-28 11:09:11', '2022-11-30 16:00:00', 2, ' 思政实践 ', ' mind', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (36, 1, ' 蓝桥杯报名', '2022-10-28 11:33:16', '2022-11-30 16:00:00', 1, ' 咨询A或者B', ' lanqiaobei', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (37, 1, ' 算法慕课作业', '2022-10-27 03:37:56', '2022-11-27 23:00:00', 4, ' 算法慕课作业截止20号晚23点', ' suanfa', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (38, 1, ' pygame作业', '2022-10-28 11:39:32', '2022-11-30 06:00:00', 6, '  pygame作业截止30截止', ' pagame', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (39, 1, 'Oj实验', '2022-10-28 11:41:08', '2022-11-30 23:00:00', 8, ' 算法Oj实验截止20号晚', 'oj', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (41, 1, ' 六级', '2022-10-28 11:46:20', '2022-12-09 16:00:00', 1, '  六级 12月10日截止', ' liuji', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (42, 1, '火车票学生认证', '2022-10-28 11:47:07', '2023-01-05 16:00:00', 1, ' 火车票学生认证 第十九周星期五截止', 'chuxing', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (43, 1, ' 数电课堂派', '2022-10-28 11:53:27', '2022-12-01 00:00:00', 6, '  数电课堂派截止 11.30', '数电', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (44, 1, ' 跑步', '2022-10-28 11:56:48', '2022-12-30 16:00:00', 1, '  跑步', 'paobu', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (45, 1, ' 保研目标', '2022-10-28 11:58:05', '2023-06-29 16:00:00', 2, ' 绩点！ 绩点！ 绩点！', ' baoyan', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (46, 1, ' 英语U校园', '2022-10-28 15:08:16', '2022-11-30 15:00:00', 8, '  英语U校园第三章截止30号', ' eng', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (57, 1, '计组对分易', '2022-11-05 13:09:32', '2022-11-28 09:00:00', 2, '截止11号晚11点59分', ' jizu', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (62, 1, '尔雅选修课', '2022-11-06 01:32:46', '2022-11-27 00:00:00', 2, ' 截止27号', ' erya', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (73, 1, '	概率论与数理统计考试', '2022-11-09 19:40:10', '2022-12-04 00:00:00', 2, '12/4考试 东2202	座次13	东区东二教学楼二楼', 'gailv', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (75, 1, 'Java考试', '2022-11-16 19:10:10', '2022-12-12 00:00:00', 2, '周一第三场	14:00~15:40	新区图书馆2#机房	东区图书馆	二楼', 'java', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (77, 1, 'C考试', '2022-11-16 19:15:18', '2022-12-17 00:00:00', 2, '新区图书馆1#机房	东区图书馆	二楼', 'c', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (78, 1, '计组考试', '2022-11-16 19:16:07', '2022-12-15 16:00:00', 2, '周四	16:00~17:40	东1112	东2204', 'jizu', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (79, 1, '算法考试', '2022-11-16 19:16:51', '2022-12-27 00:00:00', 2, '周二第四场	16:00~17:40	新区图书馆机房	东区图书馆	二楼', 'suanfa', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (80, 1, '数理逻辑考试', '2022-11-16 19:22:07', '2023-01-03 00:00:00', 2, '周二第四场	16:00~17:40	西71201	西七教学楼Ⅰ区	二楼', 'shuli', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (81, 1, 'C慕课', '2022-11-16 19:30:51', '2022-11-25 23:00:00', 13, '19号截止', 'c', NULL, 'Arc2022', 'Pause');
INSERT INTO `thingstable` VALUES (82, 1, '思政实践尔雅', '2022-11-16 19:34:43', '2022-11-30 00:00:00', 2, '30号截止', 'sizheng', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (83, 1, 'pygame讨论 pygame钉钉作业', '2022-11-16 21:44:10', '2022-11-30 20:00:00', 12, '7点57截止', 'pygame', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (85, 1, '数电考试', '2022-11-16 22:26:32', '2022-12-18 14:00:00', 2, '18号第三讲东1211', '数电', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (86, 1, '英语考试', '2022-11-16 22:27:46', '2022-12-18 16:00:00', 2, '第四讲 东3508', 'eng', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (91, 1, 'csp', '2022-11-21 16:56:59', '2022-12-18 00:00:00', 2, 'csp', 'csp', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (93, 1, '数电考试', '2022-11-22 18:49:12', '2022-12-08 00:00:00', 1, '15周考试', 'shudian', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (95, 1, '批改网', '2022-11-23 17:33:48', '2022-11-30 23:00:00', 2, '12.1截止', 'eng', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (96, 1, '概率论对分易', '2022-11-24 00:23:05', '2022-12-04 15:00:00', 2, '下午三点截止', 'galvlun', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (97, 1, '早请假', '2022-11-24 00:28:24', '2023-01-01 00:00:00', 2, '最后一周请假', ' qingjia', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (98, 1, ' 数电纸质作业', '2022-11-24 00:42:26', '2022-11-29 10:42:18', 2, '下周上午交', ' shudian', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (100, 1, ' 思政实践', '2022-11-24 21:46:42', '2022-11-28 00:00:00', 2, ' 下星期一下午', ' sizheng', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (101, 1, '大学习', '2022-11-24 21:55:28', '2023-01-07 00:00:00', 1, '每周一次', 'daxuexi', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (102, 1, ' 缓考办理', '2022-11-25 16:08:38', '2022-11-28 00:00:00', 8, ' 下周一交给詹', ' kaoshi', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (103, 1, '易班', '2022-11-25 22:47:56', '2022-11-30 00:00:00', 1, ' 没有学校投稿', ' yiban', NULL, 'Arc2022', 'Running');
INSERT INTO `thingstable` VALUES (105, 1, ' 服务器到期', '2022-11-26 01:20:36', '2023-11-23 00:00:00', 1, ' 一年有效期', ' server', NULL, 'Arc2022', 'Running');

-- ----------------------------
-- Table structure for user_voe_table
-- ----------------------------
DROP TABLE IF EXISTS `user_voe_table`;
CREATE TABLE `user_voe_table`  (
  `id` int(255) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_voe_table
-- ----------------------------
INSERT INTO `user_voe_table` VALUES (1, 'Arc2022', 'e9f5c5240c0bb39488e6dbfbdb1517e0', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
