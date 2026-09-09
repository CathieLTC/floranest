/*
 Navicat Premium Dump SQL

 Source Server         : Conn1
 Source Server Type    : MySQL
 Source Server Version : 80017 (8.0.17)
 Source Host           : localhost:3306
 Source Schema         : floranest_db

 Target Server Type    : MySQL
 Target Server Version : 80017 (8.0.17)
 File Encoding         : 65001

 Date: 06/07/2026 13:48:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `cart_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NULL DEFAULT 1,
  PRIMARY KEY (`cart_id`) USING BTREE,
  UNIQUE INDEX `uk_cart_user_product`(`user_id` ASC, `product_id` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `categories` VALUES (1, 'Indoor Plants', 'Plants suitable for indoor environments with low to medium sunlight.', '2026-07-04 17:38:32', '2026-07-04 17:38:32');
INSERT INTO `categories` VALUES (2, 'Outdoor Plants', 'Plants that thrive in gardens, balconies, and outdoor landscapes.', '2026-07-04 17:38:32', '2026-07-04 17:38:32');
INSERT INTO `categories` VALUES (3, 'Succulents', 'Low-maintenance plants that store water in their leaves.', '2026-07-04 17:38:32', '2026-07-04 17:38:32');
INSERT INTO `categories` VALUES (4, 'Flowering Plants', 'Beautiful flowering plants that brighten homes and gardens.', '2026-07-04 17:38:32', '2026-07-04 17:38:32');
INSERT INTO `categories` VALUES (5, 'Air Purifying Plants', 'Plants known for improving indoor air quality.', '2026-07-04 17:38:32', '2026-07-04 17:38:32');
INSERT INTO `categories` VALUES (6, 'Herbs', 'Edible and medicinal herbs for cooking and home gardening.', '2026-07-04 17:38:32', '2026-07-04 17:38:32');
INSERT INTO `categories` VALUES (7, 'Hanging Plants', 'Decorative plants suitable for hanging baskets and shelves.', '2026-07-04 17:38:32', '2026-07-04 17:38:32');
INSERT INTO `categories` VALUES (8, 'Bonsai', 'Miniature ornamental trees carefully cultivated for indoor decoration.', '2026-07-04 17:38:32', '2026-07-04 17:38:32');
INSERT INTO `categories` VALUES (9, 'Gardening Tools', 'Hand tools, watering gear and accessories to keep your plants healthy.', '2026-09-09 14:00:00', '2026-09-09 14:00:00');
INSERT INTO `categories` VALUES (10, 'Pots & Planters', 'Stylish pots and planters with proper drainage for every plant.', '2026-09-09 14:00:00', '2026-09-09 14:00:00');

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items`  (
  `order_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`order_item_id`) USING BTREE,
  INDEX `order_id`(`order_id` ASC) USING BTREE,
  INDEX `product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_items
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `total_amount` decimal(10, 2) NULL DEFAULT NULL,
  `order_status` enum('PENDING','PROCESSING','SHIPPED','DELIVERED','CANCELLED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'PENDING',
  `order_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `order_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `order_number`(`order_number` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL,
  `product_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `price` decimal(10, 2) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'ACTIVE',
  `sunlight` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `watering` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `difficulty` enum('Easy','Medium','Hard') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `temperature` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `video_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (1, 1, 'Monstera Deliciosa', 'Large tropical indoor plant with beautiful split leaves.', 35.99, 50, 'https://images.unsplash.com/photo-1614594975525-e45190c55d0b', '2026-07-04 17:40:31', '2026-07-04 18:42:15', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (2, 1, 'Snake Plant', 'Low maintenance indoor plant that tolerates low light.', 22.50, 80, 'https://images.unsplash.com/photo-1599009944997-3544a939813c', '2026-07-04 17:40:31', '2026-07-04 18:42:20', 'ACTIVE', 'Low to Bright', 'Every 2 Weeks', 'Easy', '15-30°C');
INSERT INTO `products` VALUES (3, 1, 'Peace Lily', 'Elegant flowering indoor plant that purifies air.', 28.99, 40, 'https://images.unsplash.com/photo-1575805501150-e064fbd815dd', '2026-07-04 17:40:31', '2026-07-04 18:46:14', 'ACTIVE', 'Indirect', 'Weekly', 'Medium', '18-28°C');
INSERT INTO `products` VALUES (4, 1, 'ZZ Plant', 'Extremely hardy plant perfect for beginners.', 24.99, 65, 'https://plus.unsplash.com/premium_photo-1664526284288-8c48dd6c38a7', '2026-07-04 17:40:31', '2026-07-04 18:45:55', 'ACTIVE', 'Low to Bright', 'Every 2 Weeks', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (5, 3, 'Aloe Vera', 'Medicinal succulent with thick green leaves.', 15.99, 100, 'https://images.unsplash.com/photo-1570295835271-04c05b4ed943', '2026-07-04 17:40:31', '2026-07-04 18:52:27', 'ACTIVE', 'Full Sun', 'Every 2 Weeks', 'Easy', '18-35°C');
INSERT INTO `products` VALUES (6, 3, 'Jade Plant', 'Popular succulent symbolizing prosperity.', 18.99, 70, 'https://images.unsplash.com/photo-1718702498597-4a4fcf56cf64', '2026-07-04 17:40:31', '2026-07-04 18:55:45', 'ACTIVE', 'Bright Sunlight', 'Every 2 Weeks', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (7, 5, 'Spider Plant', 'Excellent air purifier with cascading leaves.', 20.99, 75, 'https://plus.unsplash.com/premium_photo-1664543258866-f1362a2c7195', '2026-07-04 17:40:31', '2026-07-04 18:58:22', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Easy', '16-28°C');
INSERT INTO `products` VALUES (8, 5, 'English Ivy', 'Trailing vine known for filtering indoor air.', 19.99, 55, 'https://images.unsplash.com/photo-1657401923955-efe43f7d1196', '2026-07-04 17:40:31', '2026-07-04 19:02:13', 'ACTIVE', 'Indirect', 'Weekly', 'Medium', '15-25°C');
INSERT INTO `products` VALUES (9, 4, 'Orchid', 'Beautiful flowering orchid with long-lasting blooms.', 42.99, 30, 'https://images.unsplash.com/photo-1571677179476-ab32559a6c7c', '2026-07-04 17:40:31', '2026-07-04 19:04:31', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Medium', '18-28°C');
INSERT INTO `products` VALUES (10, 6, 'Basil', 'Fresh culinary herb perfect for home kitchens.', 9.99, 120, 'https://plus.unsplash.com/premium_photo-1693266635681-aeec6ec5e9a0', '2026-07-04 17:40:31', '2026-07-04 19:06:49', 'ACTIVE', 'Full Sun', '2-3 Times Weekly', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (11, 1, 'Rubber Plant', 'Glossy dark green leaves make this a striking indoor plant.', 29.99, 45, 'https://plus.unsplash.com/premium_photo-1668416114981-1d6cd2acbc7d', '2026-07-04 17:42:29', '2026-07-04 19:07:18', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Easy', '18-29°C');
INSERT INTO `products` VALUES (12, 1, 'Fiddle Leaf Fig', 'Popular decorative plant with large violin-shaped leaves.', 49.99, 30, 'https://plus.unsplash.com/premium_photo-1680127399693-96343c4e06b9', '2026-07-04 17:42:29', '2026-07-04 19:14:59', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Medium', '18-28°C');
INSERT INTO `products` VALUES (13, 1, 'Chinese Evergreen', 'Beautiful foliage plant that tolerates low light.', 26.99, 55, 'https://images.unsplash.com/photo-1586817100525-db60e5ed1672', '2026-07-04 17:42:29', '2026-07-04 19:15:41', 'ACTIVE', 'Low to Bright', 'Weekly', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (14, 7, 'Golden Pothos', 'Fast-growing hanging vine perfect for beginners.', 21.99, 70, 'https://images.unsplash.com/photo-1685156218577-0980070d50b6', '2026-07-04 17:42:29', '2026-07-04 19:18:30', 'ACTIVE', 'Indirect', 'Weekly', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (15, 7, '❌Heartleaf Philodendron', 'Trailing vine with heart-shaped leaves.', 23.99, 60, 'https://plus.unsplash.com/premium_photo-1675783105468-7fb8e0a3e7e8', '2026-07-04 17:42:29', '2026-07-04 19:20:36', 'ACTIVE', 'Indirect', 'Weekly', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (16, 5, 'Boston Fern', 'Lush fern that naturally improves indoor air quality.', 27.99, 40, 'https://images.unsplash.com/photo-1463320726281-696a485928c7', '2026-07-04 17:42:29', '2026-07-04 17:42:29', 'ACTIVE', 'Indirect', '2-3 Times Weekly', 'Medium', '16-26°C');
INSERT INTO `products` VALUES (17, 5, 'Areca Palm', 'Elegant palm known for excellent air purification.', 44.99, 25, 'https://images.unsplash.com/photo-1459411552884-841db9b3cc2a', '2026-07-04 17:42:29', '2026-07-04 17:42:29', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Medium', '20-30°C');
INSERT INTO `products` VALUES (18, 6, 'Mint', 'Fresh aromatic herb ideal for teas and cooking.', 8.99, 100, 'https://images.unsplash.com/photo-1615485925600-97237c4fc1ec', '2026-07-04 17:42:29', '2026-07-04 17:42:29', 'ACTIVE', 'Full Sun', '2-3 Times Weekly', 'Easy', '15-28°C');
INSERT INTO `products` VALUES (19, 6, 'Rosemary', 'Fragrant herb commonly used in Mediterranean cuisine.', 10.99, 80, 'https://images.unsplash.com/photo-1471193945509-9ad0617afabf', '2026-07-04 17:42:29', '2026-07-04 17:42:29', 'ACTIVE', 'Full Sun', 'Weekly', 'Easy', '15-30°C');
INSERT INTO `products` VALUES (20, 2, 'Bird of Paradise', 'Large tropical outdoor plant with stunning orange flowers.', 59.99, 20, 'https://images.unsplash.com/photo-1465379944081-7f47de8d74ac', '2026-07-04 17:42:29', '2026-07-04 17:42:29', 'ACTIVE', 'Full Sun', 'Weekly', 'Medium', '20-35°C');
INSERT INTO `products` VALUES (21, 1, 'Weeping Fig', 'Classic indoor tree with graceful arching branches.', 39.99, 35, 'https://images.unsplash.com/photo-1466692476868-aef1dfb1e735', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Medium', '18-28°C');
INSERT INTO `products` VALUES (22, 1, 'Calathea Orbifolia', 'Beautiful tropical foliage with large striped leaves.', 37.99, 30, 'https://images.unsplash.com/photo-1520412099551-62b6bafeb5bb', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Indirect', 'Weekly', 'Medium', '18-28°C');
INSERT INTO `products` VALUES (23, 1, 'Parlor Palm', 'Elegant palm ideal for offices and homes.', 32.99, 45, 'https://images.unsplash.com/photo-1459411552884-841db9b3cc2a', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (24, 1, 'Dracaena Marginata', 'Low-maintenance indoor plant with slender leaves.', 27.99, 50, 'https://images.unsplash.com/photo-1512428813834-c702c7702b78', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Bright Indirect', 'Every 10 Days', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (25, 3, 'Ponytail Palm', 'Unique succulent with a swollen trunk and long leaves.', 31.99, 40, 'https://images.unsplash.com/photo-1509423350716-97f2360af9f0', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Full Sun', 'Every 2 Weeks', 'Easy', '18-35°C');
INSERT INTO `products` VALUES (26, 3, 'String of Pearls', 'Cascading succulent perfect for hanging baskets.', 19.99, 55, 'https://images.unsplash.com/photo-1501004318641-b39e6451bec6', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Bright Light', 'Every 2 Weeks', 'Medium', '18-30°C');
INSERT INTO `products` VALUES (27, 3, 'Burro\'s Tail', 'Trailing succulent with thick overlapping leaves.', 18.99, 60, 'https://images.unsplash.com/photo-1459156212016-c812468e2115', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Bright Light', 'Every 2 Weeks', 'Easy', '18-32°C');
INSERT INTO `products` VALUES (28, 3, 'Christmas Cactus', 'Popular flowering cactus blooming during winter.', 22.99, 45, 'https://images.unsplash.com/photo-1596541223130-5d31a73fb6c6', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Bright Indirect', 'Weekly', 'Easy', '18-28°C');
INSERT INTO `products` VALUES (29, 6, 'Lavender', 'Fragrant herb producing beautiful purple flowers.', 12.99, 70, 'https://images.unsplash.com/photo-1471193945509-9ad0617afabf', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Full Sun', 'Weekly', 'Easy', '15-30°C');
INSERT INTO `products` VALUES (30, 6, 'Chamomile', 'Medicinal herb commonly used for herbal tea.', 11.99, 80, 'https://images.unsplash.com/photo-1615485925600-97237c4fc1ec', '2026-07-04 17:43:49', '2026-07-04 17:43:49', 'ACTIVE', 'Full Sun', '2-3 Times Weekly', 'Easy', '15-28°C');
INSERT INTO `products` VALUES (31, 5, 'Chinese Elm Bonsai', 'Beautiful miniature tree suitable for beginners.', 58.99, 18, 'https://images.unsplash.com/photo-1512428813834-c702c7702b78', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Partial Sun', 'Moderate', 'Medium', '16-28°C');
INSERT INTO `products` VALUES (32, 5, 'Juniper Bonsai', 'Classic bonsai with dense green foliage.', 65.00, 12, 'https://images.unsplash.com/photo-1513836279014-a89f7a76ae86', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Full Sun', 'Moderate', 'Hard', '15-27°C');
INSERT INTO `products` VALUES (33, 5, 'Ficus Bonsai', 'Popular indoor bonsai with glossy leaves.', 54.50, 20, 'https://images.unsplash.com/photo-1463936575829-25148e1db1b8', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Bright Indirect', 'Moderate', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (34, 5, 'Japanese Maple Bonsai', 'Elegant bonsai with colorful seasonal leaves.', 72.99, 8, 'https://images.unsplash.com/photo-1490750967868-88aa4486c946', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Partial Sun', 'Moderate', 'Hard', '15-25°C');
INSERT INTO `products` VALUES (35, 6, 'Sweet Basil', 'Fresh basil ideal for cooking and home gardens.', 8.99, 55, 'https://images.unsplash.com/photo-1501004318641-b39e6451bec6', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Full Sun', 'Frequent', 'Easy', '20-30°C');
INSERT INTO `products` VALUES (36, 6, 'Mint', 'Fast-growing aromatic herb perfect for teas.', 7.50, 60, 'https://images.unsplash.com/photo-1461354464878-ad92f492a5a0', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Partial Sun', 'Frequent', 'Easy', '18-28°C');
INSERT INTO `products` VALUES (37, 6, 'Rosemary', 'Woody herb with fragrant needle-like leaves.', 9.99, 45, 'https://images.unsplash.com/photo-1471193945509-9ad0617afabf', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Full Sun', 'Light', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (38, 6, 'Thyme', 'Compact herb commonly used in Mediterranean dishes.', 8.50, 40, 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Full Sun', 'Light', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (39, 6, 'Parsley', 'Nutritious herb rich in vitamins and flavor.', 7.99, 50, 'https://images.unsplash.com/photo-1465146344425-f00d5f5c8f07', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Partial Sun', 'Moderate', 'Easy', '18-28°C');
INSERT INTO `products` VALUES (40, 6, 'Oregano', 'Hardy herb perfect for pizza and pasta dishes.', 8.75, 42, 'https://images.unsplash.com/photo-1466692476868-aef1dfb1e735', '2026-07-04 17:44:53', '2026-07-04 17:44:53', 'ACTIVE', 'Full Sun', 'Light', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (41, 1, 'Philodendron Pink Princess', 'Rare indoor plant prized for its stunning pink variegated foliage.', 89.99, 12, 'https://images.unsplash.com/photo-1463320726281-696a485928c7', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Bright Indirect', 'Moderate', 'Medium', '18-28°C');
INSERT INTO `products` VALUES (42, 1, 'Alocasia Polly', 'Beautiful tropical houseplant with dramatic arrow-shaped leaves.', 36.99, 25, 'https://images.unsplash.com/photo-1501004318641-b39e6451bec6', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Bright Indirect', 'Moderate', 'Medium', '18-30°C');
INSERT INTO `products` VALUES (43, 1, 'Calathea Orbifolia', 'Decorative foliage plant featuring large silver-striped leaves.', 39.99, 20, 'https://images.unsplash.com/photo-1466692476868-aef1dfb1e735', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Indirect Light', 'Frequent', 'Medium', '18-28°C');
INSERT INTO `products` VALUES (44, 4, 'Peace Lily', 'Elegant flowering houseplant known for improving indoor air quality.', 24.99, 35, 'https://images.unsplash.com/photo-1512428813834-c702c7702b78', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Low to Bright Indirect', 'Moderate', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (45, 4, 'Anthurium Red', 'Long-lasting tropical flowers with glossy green foliage.', 34.99, 22, 'https://images.unsplash.com/photo-1490750967868-88aa4486c946', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Bright Indirect', 'Moderate', 'Medium', '18-28°C');
INSERT INTO `products` VALUES (46, 4, 'Bird of Paradise', 'Large tropical plant producing spectacular bird-like flowers.', 69.99, 18, 'https://images.unsplash.com/photo-1459411552884-841db9b3cc2a', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Full Sun', 'Moderate', 'Medium', '20-35°C');
INSERT INTO `products` VALUES (47, 2, 'Japanese Maple', 'Ornamental outdoor tree famous for its vibrant seasonal foliage.', 95.00, 10, 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Partial Sun', 'Moderate', 'Hard', '10-28°C');
INSERT INTO `products` VALUES (48, 3, 'String of Pearls', 'Unique trailing succulent with bead-like leaves.', 27.99, 30, 'https://images.unsplash.com/photo-1596541223130-5d31a73fb6c6', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Bright Light', 'Light', 'Easy', '18-30°C');
INSERT INTO `products` VALUES (49, 5, 'Premium Juniper Bonsai', 'Carefully shaped mature bonsai perfect for collectors.', 129.99, 8, 'https://images.unsplash.com/photo-1513836279014-a89f7a76ae86', '2026-07-04 17:46:32', '2026-07-04 17:46:32', 'ACTIVE', 'Full Sun', 'Moderate', 'Hard', '15-27°C');
INSERT INTO `products` VALUES (50, 9, 'Professional Pruning Shears', 'Sharp bypass blades for clean cuts on stems and branches up to 2cm thick.', 18.99, 120, '/images/tools/pruning-shears.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (51, 9, 'Classic Watering Can', 'Galvanised steel can with a long narrow spout for precise, gentle watering.', 22.50, 90, '/images/tools/watering-can.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (52, 9, 'Hand Trowel & Fork Set', 'Stainless steel trowel and fork with ergonomic wooden handles for potting and weeding.', 14.99, 150, '/images/tools/trowel-fork.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (53, 9, 'Gardening Gloves', 'Breathable coated gloves that protect your hands from thorns and dirt.', 9.99, 200, '/images/tools/gardening-gloves.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (54, 9, 'Soil Moisture Meter', 'Tells you instantly whether your plant needs water, so there is no more guesswork.', 12.99, 110, '/images/tools/moisture-meter.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (55, 9, 'Plant Mister Spray Bottle', 'Produces a fine mist for humidity-loving plants and gentle leaf cleaning.', 11.49, 130, '/images/tools/plant-mister.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (56, 10, 'Terracotta Pot 6 inch', 'Classic terracotta pot with a drainage hole, perfect for repotting small plants.', 7.99, 300, '/images/pots/terracotta-pot.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (57, 10, 'Glazed Ceramic Pot Set', 'Two-tone glazed ceramic planters with drainage holes and wooden saucers.', 24.99, 90, '/images/pots/ceramic-pot-set.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (58, 10, 'Hanging Macrame Planter', 'Handmade cotton macrame hanger with a white planter, fits 4-5 inch pots.', 16.99, 75, '/images/pots/macrame-planter.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (59, 10, 'Self-Watering Pot 8 inch', 'Built-in water reservoir keeps soil consistently moist for up to 2 weeks.', 19.99, 120, '/images/pots/self-watering-pot.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (60, 10, 'Modern Minimalist Planter', 'Matte finish planter with a built-in drip tray for a clean, tidy look.', 21.99, 100, '/images/pots/minimalist-planter.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);
INSERT INTO `products` VALUES (61, 10, 'Succulent Pot Set', 'Four small ceramic pots ideal for succulents, cacti and small herbs.', 17.99, 140, '/images/pots/succulent-pot-set.jpg', '2026-09-09 14:00:00', '2026-09-09 14:00:00', 'ACTIVE', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `full_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `country` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` enum('CUSTOMER','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'CUSTOMER',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'cate@gmail.com', '123456', 'Cate Diva', NULL, NULL, NULL, NULL, 'CUSTOMER', '2026-07-03 17:08:52', '2026-07-03 17:08:52');
INSERT INTO `users` VALUES (2, 'wmht46oxf@mozmail.com', '123456', 'Cissy Diva', NULL, NULL, NULL, NULL, 'CUSTOMER', '2026-07-04 12:11:50', '2026-07-04 12:11:50');
INSERT INTO `users` VALUES (3, 'leo@gmail.com', '123456', 'Leo Francisco', NULL, NULL, NULL, NULL, 'CUSTOMER', '2026-07-04 13:09:36', '2026-07-04 13:09:36');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for reviews
-- ----------------------------
CREATE TABLE `reviews`  (
  `review_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL,
  `review_type` enum('plant','shop') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'plant',
  `target_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `author_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rating` int(11) NOT NULL DEFAULT 5,
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reviews
-- ----------------------------
INSERT INTO `reviews` (`user_id`, `review_type`, `target_name`, `author_name`, `rating`, `comment`, `created_at`) VALUES
(1, 'plant', 'Monstera Deliciosa', 'Cate Diva', 5, 'Absolutely love this plant! The leaves arrived perfect and it has already grown two new ones.', '2026-09-02 09:15:00'),
(2, 'shop', 'FloraNest', 'Cissy Diva', 4, 'Great shop, plants are healthy and packaging was solid. Delivery took a couple days longer than expected.', '2026-09-04 14:40:00'),
(3, 'plant', 'Snake Plant', 'Leo Francisco', 5, 'Very low maintenance and looks amazing on my desk. Highly recommend for beginners!', '2026-09-06 18:05:00');

-- ----------------------------
-- Upgrade script for EXISTING databases (ran already has city/country)
-- Run manually against an already-created floranest_db:
--   ALTER TABLE `users`
--     ADD COLUMN `city` varchar(100) NULL DEFAULT NULL AFTER `address`,
--     ADD COLUMN `country` varchar(100) NULL DEFAULT NULL AFTER `city`;
-- ----------------------------
