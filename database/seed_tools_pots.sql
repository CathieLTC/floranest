/*
 * Incremental seed script — add the missing "Gardening Tools" / "Pots & Planters"
 * categories (9 & 10) and their products (50-61) to an EXISTING floranest_db.
 *
 * Safe to run repeatedly (INSERT IGNORE). Run against database `floranest_db`.
 *
 * Images referenced below live in: frontend/public/images/tools  and  frontend/public/images/pots
 */

-- 1) Categories 9 & 10
INSERT IGNORE INTO `categories` (`category_id`, `category_name`, `description`)
VALUES (9, 'Gardening Tools', 'Hand tools, watering gear and accessories to keep your plants healthy.'),
       (10, 'Pots & Planters', 'Stylish pots and planters with proper drainage for every plant.');

-- 2) Products 50-61 (Gardening Tools -> category 9)
INSERT IGNORE INTO `products`
    (`product_id`, `category_id`, `product_name`, `description`, `price`, `stock`, `image_url`, `status`)
VALUES
    (50, 9, 'Professional Pruning Shears', 'Sharp bypass blades for clean cuts on stems and branches up to 2cm thick.', 18.99, 120, '/images/tools/pruning-shears.jpg', 'ACTIVE'),
    (51, 9, 'Classic Watering Can', 'Galvanised steel can with a long narrow spout for precise, gentle watering.', 22.50, 90, '/images/tools/watering-can.jpg', 'ACTIVE'),
    (52, 9, 'Hand Trowel & Fork Set', 'Stainless steel trowel and fork with ergonomic wooden handles for potting and weeding.', 14.99, 150, '/images/tools/trowel-fork.jpg', 'ACTIVE'),
    (53, 9, 'Gardening Gloves', 'Breathable coated gloves that protect your hands from thorns and dirt.', 9.99, 200, '/images/tools/gardening-gloves.jpg', 'ACTIVE'),
    (54, 9, 'Soil Moisture Meter', 'Tells you instantly whether your plant needs water, so there is no more guesswork.', 12.99, 110, '/images/tools/moisture-meter.jpg', 'ACTIVE'),
    (55, 9, 'Plant Mister Spray Bottle', 'Produces a fine mist for humidity-loving plants and gentle leaf cleaning.', 11.49, 130, '/images/tools/plant-mister.jpg', 'ACTIVE');

-- 3) Products 56-61 (Pots & Planters -> category 10)
INSERT IGNORE INTO `products`
    (`product_id`, `category_id`, `product_name`, `description`, `price`, `stock`, `image_url`, `status`)
VALUES
    (56, 10, 'Terracotta Pot 6 inch', 'Classic terracotta pot with a drainage hole, perfect for repotting small plants.', 7.99, 300, '/images/pots/terracotta-pot.jpg', 'ACTIVE'),
    (57, 10, 'Glazed Ceramic Pot Set', 'Two-tone glazed ceramic planters with drainage holes and wooden saucers.', 24.99, 90, '/images/pots/ceramic-pot-set.jpg', 'ACTIVE'),
    (58, 10, 'Hanging Macrame Planter', 'Handmade cotton macrame hanger with a white planter, fits 4-5 inch pots.', 16.99, 75, '/images/pots/macrame-planter.jpg', 'ACTIVE'),
    (59, 10, 'Self-Watering Pot 8 inch', 'Built-in water reservoir keeps soil consistently moist for up to 2 weeks.', 19.99, 120, '/images/pots/self-watering-pot.jpg', 'ACTIVE'),
    (60, 10, 'Modern Minimalist Planter', 'Matte finish planter with a built-in drip tray for a clean, tidy look.', 21.99, 100, '/images/pots/minimalist-planter.jpg', 'ACTIVE'),
    (61, 10, 'Succulent Pot Set', 'Four small ceramic pots ideal for succulents, cacti and small herbs.', 17.99, 140, '/images/pots/succulent-pot-set.jpg', 'ACTIVE');
