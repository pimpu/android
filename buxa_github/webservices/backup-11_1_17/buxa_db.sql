-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 01, 2016 at 12:17 PM
-- Server version: 5.5.8
-- PHP Version: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `buxa_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `bx_cfs_addresses`
--

CREATE TABLE IF NOT EXISTS `bx_cfs_addresses` (
  `cfs_id` int(11) NOT NULL AUTO_INCREMENT,
  `cfs_address` longtext NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`cfs_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=34 ;

--
-- Dumping data for table `bx_cfs_addresses`
--

INSERT INTO `bx_cfs_addresses` (`cfs_id`, `cfs_address`, `status`) VALUES
(1, 'Central Ware Housing Corporation, Kalamboli, Navi Mumbai - 400218', 1),
(2, 'CWC Dronagiri, Node Navi Mumbai - 400707', 1),
(3, 'CWC, Logistics Park, (Hind Terminal)Distripark D''node, Navi Mumbai - 400707', 1),
(4, 'CWC, Impex Park, Sector-1, D''node, Tal : Uran, Dist. Raigad, Maharashtra - 400707', 1),
(5, 'CWC, Distripark Ltd., Sector - 7, D''node, Navi Mumbai - 400707', 1),
(6, 'Speedy Multimode Ltd, CFS, Jawaharlal Nehru Port, Navi Mumbai', 1),
(7, 'Maharasthra State Warehousing Corpn., Plot No. 89, Sector-1, D''node, Sheva, Navi Mumbai - 400707', 1),
(8, 'Container Corporation Of India Ltd., Dronagiri Rail Terminal, Sector 2, Plot No. 33, 34 35, Navi Mumbai-400707', 1),
(9, 'Balmer & Lawrie Co. Ltd., Sector No. 7, Plot No.1, P.O. Box No. 8, Dronagiri Road, Navi Mumbai - 400707', 1),
(10, 'Punjab State Container & Warehousing corp Ltd.,(A Govt. of Punjab undertaking), CFS, Plot No./ 2, Sector - 2, Dronagiri Node, Navi Mumbai - 410 707', 1),
(11, 'Gateway Distriparks Ltd, Sector-6, Dronagiri, Dist-Raigad, Navi Mumbai - 400707', 1),
(12, 'United Liner Agencies of India (P) Ltd., Sector - 8, Dronagiri, P.O. Box No. 5, (jnpt), Opp Bhendhkal Village, Tal Uran, Dist Raigad, Navi Mumbai', 1),
(13, 'APM (Maersk India Pvt. Ltd), CFS Divn., Lot No. 100, Block No. 5, Sector 2, Dronagiri Warehousing Complex, Navi Mumbai - 400707', 1),
(14, 'APM (Maersk India Pvt. Ltd), CFS Divn. (Annex), Plot No. - 5-18, Sector-6, Dronagiri, Navi Mumbai', 1),
(15, 'All Cargo Logistics Ltd., CFS, Village - Koprali (JNP Area), Tal. Uran, Dist. - Raigad, Maharashtra - 410212', 1),
(16, 'TG TERMINALS Pvt LTD., Veshvi, Navi Mumbai - 400702', 1),
(17, 'Continental Warehousing (Nhava Sheva) Ltd., D.No. 1088, Khopta Village, Taluka Uran, Dist Raigad, Navi Mumbai', 1),
(18, 'Seabird Marine Services Pvt Ltd., Plot No. 70-81, Sector - 1, Dronagiri Node, Navi Mumbai - 400707', 1),
(19, 'JWC Logistics Park Ltd, 69-91, National Highway No. 17, Palspe Panvel, Maharashtra - 410206', 1),
(20, 'Preeti Logistics Ltd. (Old), Survery No. 137/1A/1, At New Ajivali Village, Old Mumbai - Pune NH-4, Tal Panvel - 410212', 1),
(21, 'Preeti Logistics Ltd (New), CFS Survey No. 137/1A/1, New Ajivali Village, Old Mumbai - Pune NH4, Tal. Panvel, Dist Raigad, Maharashtra - 410212', 1),
(22, 'Ameya Logistics Pvt. Ltd., Village Dhasakhoshi, Taluka - Uran, Post-Khopte, Dist : Raigad - 410212', 1),
(23, 'Ashte Logistics Pvt. Ltd., Ashte Village, Taluke Panvel, Dist. Raigad', 1),
(24, 'Navkar Corporation Ltd., Somatane, on Kon - Savla Road, Taluka - Panvel, Dist - Raigad', 1),
(25, 'Apollo Logisolutions Ltd., Kone Rasayani Road, Somatane Village, Tal : Panvel, Dist : Raigad, Maharashtra', 1),
(26, 'Ocean Gate Container Terminals Pvt. Ltd., Plot No. 75-1/A/B, 75-2/A/D/K, 77-3/A, 86-2, Phalaspe, Panvel-Goa Highway, Panvel, Raigad.', 1),
(27, 'Vaishno Logistics Yard, CFS, Near Chirle Village, At-Post-Jasai, Tal. Uran, Dist. Raigad - 410206.', 1),
(28, 'Indev Logistics Pvt. Ltd., Plot No. 10, Somathane Village, Kone-Savla Rasayani Road, SH 82, Panvel Taluka, Raigad-410206', 1),
(29, 'M/s SBW Logistics Pvt. Ltd, Gutt No.55, Village-Khairane, Taluka - Panvel , Dist. - Raigad, Maharashtra', 1),
(30, 'M/s. Transindia Logistics Park Pvt. Ltd., At-Khopta, Po-Koprali, Tal-Uran, Raigad., Pin - 410212, Maharashtra', 1),
(31, 'M/s. JWR Logistics Pvt. Ltd., 13-45, National Highway 4B, Panvel-JNPT Highway, Village Padeghar, Post Kundevahal, Panvel - 410206. Maharashtra', 1),
(32, 'EFC Logistics India Pvt Ltd., Village-Veshvi, Post-Dighore, Taluka-Uran, Dist-Raigad-410207', 1),
(33, 'TAKE CARE LOGISTICSPARK (INDIA) PVT LTD, SURVEY NO.96/1,87/3,4,5 & 6, MUMBAI GOA HIGHWAY, PALASPE VILLAGE, PANVEL, Dist-Raigad-410206', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bx_commodity`
--

CREATE TABLE IF NOT EXISTS `bx_commodity` (
  `comid` int(11) NOT NULL AUTO_INCREMENT,
  `comodity` longtext NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`comid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=477 ;

--
-- Dumping data for table `bx_commodity`
--

INSERT INTO `bx_commodity` (`comid`, `comodity`, `status`) VALUES
(1, 'Alcoholic beverages , nos', 1),
(2, 'Aluminium , aluminium articles , metal', 1),
(3, 'Animal fats , animal oils , lards', 1),
(4, 'Animal fodder , pet food , frozen', 1),
(5, 'Animal fodder , pet food , non-frozen', 1),
(6, 'Autoparts, car parts, vehicle parts, motorcycle parts , bicyde parts , used', 1),
(7, 'Avocado , frozen fruit', 1),
(8, 'Avocado , non- frozen , fruit', 1),
(9, 'Asparagus , frozen , vegetables', 1),
(10, 'Asparagus , non-frozen , vegetables', 1),
(11, 'Aubergine , eggplant , frozen , vegetables', 1),
(12, 'Aubergine , eggplant , non-frozen , vegetables', 1),
(13, 'Autoparts, car parts, vehicle parts, motorcycle parts , bicyde parts ,new', 1),
(14, 'Autoparts, car parts, vechicle parts', 1),
(15, 'Artificial flowers', 1),
(16, 'Apple concentrate , apple juice ,frozen , foodstuff', 1),
(17, 'Apple concentrate , apple juice , non- frozen , foodstuff', 1),
(18, 'Apples , frozen , fruit', 1),
(19, 'Apples , non-frozen , fruit', 1),
(20, 'Apricot , frozen. Fruit', 1),
(21, 'Apricot , non-frozen. Fruit', 1),
(22, 'Art , antiques, collectors'' pieces, Bric -a-brac', 1),
(23, 'Artichokes, Frozen , Vegetables', 1),
(24, 'Artichokes , non-frozen, Vegetables', 1),
(25, 'Bairdi crab, frozen , sheallfish', 1),
(26, 'Bairdi crab, non-frozen , sheallfish', 1),
(27, 'Banana , plantains , frozen , fruit', 1),
(28, 'Banana , plantains , non-frozen , fruit', 1),
(29, 'Base metals , base metal articles ', 1),
(30, 'Batteries', 1),
(31, 'Beans , frozen, vegrtables', 1),
(32, 'Beans , non- frozen , vegetables', 1),
(33, 'Beef , meat , frozen', 1),
(34, 'Beef , meat ,non-frozen', 1),
(35, 'Beer , beverages', 1),
(36, 'Beetroot , red beet , celeriac , radishes , frozen , vegetables', 1),
(37, 'Beetroot , red beet , celeriac , radishes , non-frozen , vegetables', 1),
(38, 'Berries nos , frozen', 1),
(39, 'Berries nos ,non- frozen', 1),
(40, 'Beverages , softdrink', 1),
(41, 'Brussels sprouts , frozen vegetables', 1),
(42, 'Brussels sprouts ,non- frozen vegetables', 1),
(43, 'Butter , fats , oils , frozen', 1),
(44, 'Butter , fats , oils , non-frozen', 1),
(45, 'Cabbage , frozen , vegetables', 1),
(46, 'Cabbage , non-frozen , vegetables', 1),
(47, 'Calcium hypochlorite', 1),
(48, 'Books , newspapers , pictures , printed matter', 1),
(49, 'Bottom fish , nos, frozen, fish', 1),
(50, 'Bottom fish , nos, non-frozen, fish', 1),
(51, 'Bovine Semen , Frozen', 1),
(52, 'Broccoil , Cauliflower , frozen , vegetables', 1),
(53, 'Broccoil , Cauliflower , non-frozen , vegetables', 1),
(54, '3kg , frozen', 1),
(55, 'Black fermented tea in packings > 3kg , frozen', 1),
(56, 'Black fermented tea in packings > 3kg , non frozen', 1),
(57, 'Blood plasma', 1),
(58, 'Blueberries , raspberries , frozen , berries', 1),
(59, 'Blueberries , raspberries , non-frozen , berries', 1),
(60, 'Bone meal , non frozen', 1),
(61, 'Black cod , frozen, fish', 1),
(62, 'Black cod , non-frozen, fish', 1),
(63, 'Black fermented tea in packings <= 3kg , frozen', 1),
(64, 'Black fermented tea in packings <=3kg , non frozen', 1),
(65, 'Bi - valve mollusks , live shellfish', 1),
(66, 'Bitter gourd , cherimoya, chillies, courgette ,zucchini , fennels , sugar peas, scorzoner , taro , malanga, parsnips , pak chois , okra , ladies, finger , rhubarb , frozen , vegetables', 1),
(67, 'Bitter gourd , cherimoya, chillies, courgette ,zucchini , fennels , sugar peas, scorzoner , taro , malanga, parsnips , pak chois , okra , ladies, ', 1),
(68, 'Beef , meat , frozen', 1),
(69, 'Cars Knocked Down', 1),
(70, 'Celery, Frozen , Vegetables', 1),
(71, 'Celery , Non- Frozen , Vegetables', 1),
(72, 'Ceramics , Stoneware', 1),
(73, 'Cereals , Pop corn , Wheat , Corn , rye , oats , corn , rice ,', 1),
(74, 'Cheese , Fresh , Greated ,or powdered non- frozen', 1),
(75, 'Cheese , Frozen ', 1),
(76, 'Carambolas , starfruits, durians,che-nee, feijoas , jackfruit, kumquats, whortleberries, persimmon, kaki, passion fruit, parsley , longen, loquats, hee, lychee, mon-thong , cha-nee, pricky pear, non- frozen , fruit', 1),
(77, 'Carpets, floor converings , textiles', 1),
(78, 'carrots , turnips , frozen , vegetables', 1),
(79, 'Carrots , turnips , non- frozen', 1),
(80, 'Candles , tapers', 1),
(81, 'Carambolas , starfruits, durians,che-nee, feijoas , jackfruit,', 1),
(82, 'Chemical products, nos cherries , frozen berries cherries , non-frozen , berries chestnuts ', 1),
(83, 'Chicory , frozen, vegetables', 1),
(84, 'Chicory , non-frozen, vegetables', 1),
(85, 'chocolate , frozen , foodstuff', 1),
(86, 'chocolate ,non- frozen , foodstuff', 1),
(87, 'Cigarettes', 1),
(88, 'Citrus various ,orange, frozen , fruit', 1),
(89, 'Citrus various ,orange, non-frozen , fruit', 1),
(90, 'Clay', 1),
(91, 'Clementine , mandarins , tangerines , frozen , fruit', 1),
(92, 'Clementine , mandarins , tangerines , non-frozen , fruit', 1),
(93, 'Cranberries , frozen , berries', 1),
(94, 'Cranberries , non-frozen , berries', 1),
(95, 'Crustaceans , live , shellfish', 1),
(96, 'Cucumber , frozen , vegetables', 1),
(97, 'Cucumber ,non- frozen , vegetables', 1),
(98, 'Cut Flowers', 1),
(99, 'Cuttle fish , frozen , shellfish', 1),
(100, 'Cuttle fish ,non- frozen , shellfish', 1),
(101, 'Cyanide , cyanide products', 1),
(102, 'Coffee , roasted, non-frozen', 1),
(103, 'Coffee , roasted, frozen', 1),
(104, 'Computers,computers parts, new (Laptops)', 1),
(105, 'Computers,computers parts, used (Laptops)', 1),
(106, 'Confectionery , candy , foodstuff', 1),
(107, 'Consolidated Cargo', 1),
(108, 'Cod stomachs , pollock stomachs, frozen , fish', 1),
(109, 'Cod stomachs , pollock stomachs, non-frozen , fish', 1),
(110, 'Coffee extracts , frozen', 1),
(111, 'Coffee , non- roasted , frozen', 1),
(112, 'Coffee , non- roasted ,non- frozen', 1),
(113, 'Coffee , roasted ,non- frozen', 1),
(114, 'Clethodim', 1),
(115, 'Clocks , watches and parts thereof', 1),
(116, 'Cocoa beans , foodstuff', 1),
(117, 'Cocoa butter , foodstuff', 1),
(118, 'Cocoa powder , foodstuff', 1),
(119, 'Coconuts , brazil nuts, cashew nuts', 1),
(120, 'Cod fish frozen , fish', 1),
(121, 'Cod Fish , non-frozen, fish', 1),
(122, 'Cod milt , pollock milt , frozen , fish', 1),
(123, 'Cod milt , pollock milt , non-frozen , fish', 1),
(124, 'Cod roe, frozen , fish', 1),
(125, 'Cod roe, non-frozen , fish', 1),
(126, 'Cod stomachs, pollock stomachs, frozen, fish', 1),
(127, 'Cod stomachs, pollock stomachs, non-frozen, fish', 1),
(128, 'Construction and building material', 1),
(129, 'Construction and building material, insulation meterial,metal', 1),
(130, 'Copper, copper articles , metal', 1),
(131, 'Copper , copper articles , metal', 1),
(132, 'Copper , refined , metal', 1),
(133, 'Cork, articles of cork', 1),
(134, 'corn , baby, frozen, vegetables', 1),
(135, 'corn , baby,non- frozen, vegetables', 1),
(136, 'Cotton , textiles', 1),
(137, 'Crab pots,frozen, shellfish', 1),
(138, 'Crab pots,non-frozen, shellfish', 1),
(139, 'Crab waste , frozen , shellfish', 1),
(140, 'Crab waste , non-frozen , shellfish', 1),
(141, 'Crab , canned,preserved, frozen , shellfish', 1),
(142, 'Crab , canned,preserved, non-frozen , shellfish', 1),
(143, 'Crab , nos, frozen , shellfish', 1),
(144, 'Dairy Products, frozen , nos', 1),
(145, 'Dairy Products,non- frozen , nos', 1),
(146, 'Dates , frozen, fruit', 1),
(147, 'Dates , non-frozen, fruit', 1),
(148, 'Detergents', 1),
(149, 'Diplomatic cargo', 1),
(150, 'Eel, frozen , fish', 1),
(151, 'Eel, non-frozen , fish', 1),
(152, 'Eggs all kind , frozen', 1),
(153, 'Eggs all kind , non-frozen', 1),
(154, 'Electronics , electronic appliances, audio, video equipment, telecommunication equipment. New (Cell phones, Mobile phones)', 1),
(155, 'Electronics , electronic appliances, audio, video equipment, telecommunication equipment.used(cell phones, mobile phones)', 1),
(156, 'Empty tanks , containers, metal', 1),
(157, 'Endive, escarole, frozen, vegetabls', 1),
(158, 'Endive, escarole, non-frozen, vegetabls', 1),
(159, 'Enzymes', 1),
(160, 'Epoxy Resin, plastic', 1),
(161, 'Esparto , other plaiting meterials and articles of esparto, other plaiting materials', 1),
(162, 'Essential oils', 1),
(163, 'Explosives , pyrotechnic products, nos', 1),
(164, 'Fabrics , nos, textiles', 1),
(165, 'Fertilizers', 1),
(166, 'Figs , frozen, fruit', 1),
(167, 'Figs , non-frozen, fruit', 1),
(168, 'Fireworks', 1),
(169, 'Fish bones, unprocessed', 1),
(170, 'Fish fats, fish oils', 1),
(171, 'Fish Fillet , dired, salted', 1),
(172, 'Fish fillets, frozen', 1),
(173, 'Fish fillets, non-frozen', 1),
(174, 'Fish meal', 1),
(175, 'Fish others, ULT(-60C/-76F)', 1),
(176, 'Fish roe , frozen', 1),
(177, 'Fish roe ,non- frozen', 1),
(178, 'Fish, dried', 1),
(179, 'Fish, nos, frozen', 1),
(180, 'Fish, nos, non-frozen', 1),
(181, 'Flour all kinds', 1),
(182, 'Flower bulbs', 1),
(183, 'Foliage , grass, moss', 1),
(184, 'Food Culture, ULT(-60C/-76F), foodstuff', 1),
(185, 'Foodstuff, nos, frozen', 1),
(186, 'Foodstuff, nos, non-frozen', 1),
(187, 'Footwear , new , apparel(Sandals, boots, shoes)', 1),
(188, 'Footwear , used , apparel(Shoes, Sandals, Boots)', 1),
(189, 'Freanch fries, IQF, frozen , vegetables', 1),
(190, 'Freanch fries, IQF,non- frozen , vegetables', 1),
(191, 'Freash or chilled truffles, vegetables', 1),
(192, 'Frongs'' legs. Frozen', 1),
(193, 'Frongs'' legs. non-Frozen', 1),
(194, 'Fruits, nuts, dried, nos, non frozen', 1),
(195, 'Fruits, nuts, dried, nos, frozen', 1),
(196, 'Fuel wood , charcoal', 1),
(197, 'Furniture, nos', 1),
(198, 'Furskins, artificial fur', 1),
(199, 'Garlic dried, frozen, vegetables', 1),
(200, 'Garlic dried,non- frozen, vegetables', 1),
(201, 'Garments, apparel, new', 1),
(202, 'Garments, apparel, used', 1),
(203, 'Ginger', 1),
(204, 'Glass, glassware', 1),
(205, 'Gloves, surgical gloves, rubber', 1),
(206, 'Gooseberries, pineapple cherries, non-frozen, berries', 1),
(207, 'Grapefruit, frozen, fruit', 1),
(208, 'Grapefruit, non-frozen, fruit', 1),
(209, 'Grapes, frozen, fruit', 1),
(210, 'Grapes, non-frozen, fruit', 1),
(211, 'Guavas, mangoes, mangoes, guavas, mangosteen, frozen, fruit', 1),
(212, 'Guavas, mangoes, mangoes, guavas, mangosteen, non-frozen, fruit', 1),
(213, 'Halibut, frozen, fish', 1),
(214, 'Halibut, non-frozen, fish', 1),
(215, 'Handbags, leather, trunks, suitcase, briefcase, wallets, purses, back-packs, school bags', 1),
(216, 'Handtools', 1),
(217, 'hats, caps,new, apparel', 1),
(218, 'Hats, caps, used, apparel', 1),
(219, 'Hay, Straw and articles of straw', 1),
(220, 'Helium, liquid or gas', 1),
(221, 'Herring roe, frozen, fish', 1),
(222, 'Herring roe, non-frozen, fish', 1),
(223, 'Herring, frozen, fish', 1),
(224, 'Herring, non-frozen, fish', 1),
(225, 'Honey, frozen', 1),
(226, 'Honey, non-frozen', 1),
(227, 'Horse Mackerel', 1),
(228, 'Household articles of iron and steel, metal', 1),
(229, 'Household goods, personal effects, not for resale for public distribution', 1),
(230, 'Hydrazine', 1),
(231, 'Ice cream, ULT(-60C/-76F), foodstuff', 1),
(232, 'Ice cream, frozen, foodstuff', 1),
(233, 'Impregnated textile fabrics', 1),
(234, 'Inorganic chemical, nos', 1),
(235, 'Iron ore', 1),
(236, 'Iron, steel, iron and steel articles, metal', 1),
(237, 'Jam, jelly, marmalade, frozen, foodstuff', 1),
(238, 'Jam, jelly, marmalade, non-frozen, foodstuff', 1),
(239, 'Juice, concentrate, nos, frozen, foodstuff', 1),
(240, 'Juice, concentrate, nos, non-frozen, foodstuff', 1),
(241, 'King crab, frozen, shellfish', 1),
(242, 'King crab, non-frozen, shellfish', 1),
(243, 'Kitchen and restaurant utilites, appliance, cookware, cutlery, flatware, dinner ware,c hina, dishes', 1),
(244, 'Kiwi, frozen, fruit', 1),
(245, 'Kiwi, non-frozen, fruit', 1),
(246, 'Lactose, gums, foodstuff', 1),
(247, 'Lamb, sheep, frozen, meat', 1),
(248, 'Lamb, sheep, non-frozen, meat', 1),
(249, 'Lamps, lighting fixtures', 1),
(250, 'Lead, lead articles, metal', 1),
(251, 'Leather accessories', 1),
(252, 'Leeks, frozen, vegetables', 1),
(253, 'Leeks, non-frozen, vegetables', 1),
(254, 'Lemons, lime, frozen, fruit', 1),
(255, 'Lemons, lime, non-frozen, fruit', 1),
(256, 'Lettuce, frozen, vegetables', 1),
(257, 'Lettuce, non-frozen, vegetables', 1),
(258, 'Live fish', 1),
(259, 'Lobster, frozen, shellfish', 1),
(260, 'Lobster, non-rozen, shellfish', 1),
(261, 'Logs, lumber', 1),
(262, 'Lubricants', 1),
(263, 'Machinery or mechanical appliances, new', 1),
(264, 'Machinery or mechanical appliances, used', 1),
(265, 'Mackerel', 1),
(266, 'Malanga, frozen, vegetables', 1),
(267, 'Malanga, non-frozen, vegetables', 1),
(268, 'Malt', 1),
(269, 'Man-made filaments, textiles', 1),
(270, 'Man-made staple fibres, textiles', 1),
(271, 'Manioc, cassava, yukka, frozen, vegetables', 1),
(272, 'Manioc, cassava, yukka, non-frozen, vegetables', 1),
(273, 'Margarine, frozen', 1),
(274, 'Margarine, non-frozen', 1),
(275, 'Matches', 1),
(276, 'Mate', 1),
(277, 'Meat, nos, frozen', 1),
(278, 'Meat, nos, non-frozen', 1),
(279, 'Melons cantaloupe, honey, net, frozen, fruit', 1),
(280, 'Melons cantaloupe, honey, net, non-frozen, fruit', 1),
(281, 'Milk, cream, yoghurt, frozen', 1),
(282, 'Milk, cream, yoghurt, non-frozen', 1),
(283, 'Mineral fuels, oils, waxes', 1),
(284, 'Minerals', 1),
(285, 'Molybdenom wire, metal', 1),
(286, 'Motorized vehicles, cars, buses, trucks, lorries, motorcycles, minivan, setup, new', 1),
(287, 'Mushrooms, frozen, vegetables', 1),
(288, 'Mushrooms, non-frozen, vegetables', 1),
(289, 'Musical Instruments', 1),
(290, 'Natural or cultured pearls, precious or semi-precious stones, precious metals, metals clad with precious metal, and articles thereof, imitation jewellery, coin', 1),
(291, 'Nickel, nickel articles, metal', 1),
(292, 'Octopus, frozen, shellfish', 1),
(293, 'Octopus, non-frozen, shellfish', 1),
(294, 'Onion, frozen, starvent, vegetables', 1),
(295, 'Onion, non-frozen, starvent, vegetables', 1),
(296, 'Onion, frozen, vegetables', 1),
(297, 'Onion, non-frozen, vegetables', 1),
(298, 'Opilio crab, frozen, shellfish', 1),
(299, 'Opilio crab, non-frozen, shellfish', 1),
(300, 'Optical, photographic, cinematographic, measuring, checking, precision, medical or surgical instruments and apparatus, parts and accessories thereof', 1),
(301, 'Orange juice, frozen, foodstuff', 1),
(302, 'Orange juice, non-frozen, foodstuff', 1),
(303, 'Ores, nos', 1),
(304, 'Organic chemical, nos', 1),
(305, 'Other vegetables textile fibres, paper yarn, woven fabrics of paper yarn', 1),
(306, 'Palm oil [Olive oil]', 1),
(307, 'Papaya, frozen, fruit', 1),
(308, 'Papaya, non-frozen, fruit', 1),
(309, 'Paper, paperboard, packing', 1),
(310, 'Paperboard, KLB, kraft liner board, liner board, newsprint, mail', 1),
(311, 'Pastry, bread, cake, frozen, foodstuff', 1),
(312, 'Pastry, bread, cake, non-frozen, foodstuff', 1),
(313, 'Patlo furniture', 1),
(314, 'Peaches, nectarines, frozen, fruit', 1),
(315, 'Peaches, nectarines, non-frozen, fruit', 1),
(316, 'Pears, quinces, frozen, fruit', 1),
(317, 'pears, quinces, non-frozen, fruit', 1),
(318, 'Peas, frozen, vegetables', 1),
(319, 'Peas, non-frozen, vegetables', 1),
(320, 'Peel of melon or citrus, frozen', 1),
(321, 'Peppers capsicum, frozen, vegetables', 1),
(322, 'Peppers capsicum, non-frozen, vegetables', 1),
(323, 'Perfumes, makeup, cosmetics, toilet preparations', 1),
(324, 'Pharmaceutical products, medicaments', 1),
(325, 'Phosphates', 1),
(326, 'Photo conductor', 1),
(327, 'Photographic paper, photographic film', 1),
(328, 'Pineapple juice, frozen, foodstuff', 1),
(329, 'Pineapple juice, non-frozen, foodstuff', 1),
(330, 'Pineapple, frozen, fruit', 1),
(331, 'Pineapple, non-frozen, fruit', 1),
(332, 'Plants, potted', 1),
(333, 'plastic, plastic articles, new', 1),
(334, 'Plastic, plastic articles, used', 1),
(335, 'Plates, sheets, film, foil, plastic', 1),
(336, 'Plums, sloes, frozen, fruit', 1),
(337, 'Plums, sloes, non-frozen, fruit', 1),
(338, 'Plywood, panel, board products', 1),
(339, 'Polishes, creams', 1),
(340, 'Pollock fish, frozen, fish', 1),
(341, 'Pollock fish, non-frozen, fish', 1),
(342, 'Pollock roe, frozen, fish', 1),
(343, 'Pollock roe, non-frozen, fish', 1),
(344, 'Pork, swine, frozen, meat', 1),
(345, 'Pork, swine, non-frozen, meat', 1),
(346, 'Potatoes, frozen, vegetables', 1),
(347, 'Potatoes, non-frozen, vegetables', 1),
(348, 'Poultry, chicken, turkey, duck, fowl, frozen', 1),
(349, 'Poultry, chicken, turkey, duck, fowl, non-frozen', 1),
(350, 'Prepared feathers and down and articles made of feathers or of down, artificial flowers, articles of human hair', 1),
(351, 'Project exported in accordance with commission regulation (ec) no 840/96 ', 1),
(352, 'Provisions, food activities, vitamins, foodstuff', 1),
(353, 'Pulping and paper making', 1),
(354, 'Rags, textile waste', 1),
(355, 'Raikway Equipment', 1),
(356, 'Raw hides, skins, dry', 1),
(357, 'Raw hides, skins, wet', 1),
(358, 'Relief goods', 1),
(359, 'Rubber articles, rubber goods', 1),
(360, 'Sable fish, frozen, fish', 1),
(361, 'Sable fish, non-frozen, fish', 1),
(362, 'Sacks and bags, plastic', 1),
(363, 'Salmon roe, frozen, fish', 1),
(364, 'Salmon roe, non-frozen, fish', 1),
(365, 'Salmon, frozen, fish', 1),
(366, 'Salmon, non-frozen, fish', 1),
(367, 'Salmon, pink chum, frozen, fish', 1),
(368, 'Salmon/ Trout, ULT(-60C/-76F), fish', 1),
(369, 'Salt, sulphur, earths, stone', 1),
(370, 'Sardines, frozen, fish', 1),
(371, 'Sardines, non-frozen, fish', 1),
(372, 'Sauce, frozen, foodstuff', 1),
(373, 'Sauce, non-frozen, foodstuff', 1),
(374, 'Scrap metal', 1),
(375, 'Sea Urchin, ULT(-60C/-76F), shellfish', 1),
(376, 'Sea urchins, frozen, shellfish', 1),
(377, 'Sea urchins, non-frozen, shellfish', 1),
(378, 'Seats, convertible into beds', 1),
(379, 'Seaweed ', 1),
(380, 'Seaweed ash', 1),
(381, 'Seeds, grain, indusrtrial plants, medicinal plants', 1),
(382, 'Seeds, sesame seeds', 1),
(383, 'Shampoo', 1),
(384, 'Shellfish. IQF, nos, frozen', 1),
(385, 'Shellfish. IQF, nos, non-frozen', 1),
(386, 'Shells, crab, crushed', 1),
(387, 'Ships, boats and floating structures', 1),
(388, 'Shrimps, prawns, frozen, shellfish', 1),
(389, 'Shrimps, prawns, non-frozen, shellfish', 1),
(390, 'Silk, textiles', 1),
(391, 'Skipjack, frozen, fish', 1),
(392, 'Skipjack, non-frozen, fish', 1),
(393, 'Slag ash', 1),
(394, 'Smoked fish, frozen', 1),
(395, 'Smoked fish, non-frozen', 1),
(396, 'Snails, frozen, shellfish', 1),
(397, 'Snails, non-frozen, shellfish', 1),
(398, 'Soap', 1),
(399, 'Soup, broth, frozen, foodstuff', 1),
(400, 'Soup, broth, non-frozen, foodstuff', 1),
(401, 'Soy, Soyabeans', 1),
(402, 'Spices', 1),
(403, 'Spinach, frozen, vegetables', 1),
(404, 'Spinach, non-frozen, vegetables', 1),
(405, 'Sports goods', 1),
(406, 'Starches, glue', 1),
(407, 'Strawberries, frozen, berries', 1),
(408, 'Strawberries, non-frozen, berries', 1),
(409, 'Sugar, syrup, foodstuff', 1),
(410, 'Surimi, frozen, fish', 1),
(411, 'Surimi, non-frozen, fish', 1),
(412, 'Swedes, frozen, vegetables', 1),
(413, 'Swedes, non-frozen, vegetables', 1),
(414, 'Sweet potatoes, frozen, vegetables', 1),
(415, 'Sweet potatoes, non-frozen, vegetables', 1),
(416, 'Sweetcorn, frozen, vegetables', 1),
(417, 'Sweetcorn, non-frozen, vegetables', 1),
(418, 'Synthetic resins, plastic', 1),
(419, 'Tanning extracts, dyeing extracts, paints, varnishes', 1),
(420, 'Tea, bags, frozen', 1),
(421, 'Tea, bags, non-frozen', 1),
(422, 'Tea, loose, frozen', 1),
(423, 'Tea, loose, non-frozen', 1),
(424, 'Textiles, cotton piece goods', 1),
(425, 'Tile, stone articles', 1),
(426, 'Timber, sawn', 1),
(427, 'Tin, tin articles, metal', 1),
(428, 'Tires, tyres, rubber', 1),
(429, 'Titanium tetrachloride', 1),
(430, 'Tobacco, tobacco accessories [Cigars]', 1),
(431, 'Toilet paper, paper napkins, diapers, sanitary towels', 1),
(432, 'Tomatoes, frozen, vegetables', 1),
(433, 'Tomatoes, non-frozen, vegetables', 1),
(434, 'Toys, games', 1),
(435, 'Trichloric acid', 1),
(436, 'Tuna Albacore ULT (-60C/-76F)', 1),
(437, 'Tuna, By-Catch, ULT(-60C/-76F), fish', 1),
(438, 'Tuna, Land Processed, ULT(-60C/-76F), fish', 1),
(439, 'Tuna, NOE, ULT(-60C/-76F), fish', 1),
(440, 'Tuna, Tuna, ULT(-60C/-76F), fish', 1),
(441, 'Tuna, Yellow Fin, ULT(-60C/-76F), fish', 1),
(442, 'Tuna, yellowfin, frozen, fish', 1),
(443, 'Tuna, yellowfin, non-frozen, fish', 1),
(444, 'Tungsten wire,  metal', 1),
(445, 'Twine, cordage, ropes, cables, textiles', 1),
(446, 'Umbrellas, sun umbrellas, walking-sticks, seat-tricks, whips, riding-crops and parts thereof', 1),
(447, 'Vandium oxides, hydroxides', 1),
(448, 'Vegetable juice, frozen, foodstuff', 1),
(449, 'Vegetable juice, non-frozen, foodstuff', 1),
(450, 'Vegetable materials, vegetable products', 1),
(451, 'Vegetable saps, extracts, frozen, vgetables', 1),
(452, 'Vegetable saps, extracts, non-frozen, vgetables', 1),
(453, 'Vegetables, nos, frozen, vegetables', 1),
(454, 'Vegetables, nos, non-frozen, vegetables', 1),
(455, 'Vegetables, preserved, canned, non-frozen, foodstuff', 1),
(456, 'Vegetables, preserved, frozen, foodstuff', 1),
(457, 'Vehicles, car, buses, trucks, lorries, motorcycles, bicycles, knock down, CKD, new', 1),
(458, 'Vehicles, car, buses, trucks, lorries, motorcycles, bicycles, knock down, CKD, used', 1),
(459, 'Veneer, sliced, peeled', 1),
(460, 'Vinegar, foodstuff', 1),
(461, 'Wadding, felt, nonwovens, thread, cord, yarn, strip, textiles', 1),
(462, 'Walnuts', 1),
(463, 'Wastepaper', 1),
(464, 'Watermelon, frozen, fruit', 1),
(465, 'Watermelon, non-frozen, fruit', 1),
(466, 'Waxes', 1),
(467, 'White goods [Washing machine, Fridge, Freezer, Microwave ovens, dishwasher]', 1),
(468, 'White pigments, Titatnium dioxide', 1),
(469, 'Wine, beverages, Bottled', 1),
(470, 'Wine, Beverages, Bulk', 1),
(471, 'Wood pulp', 1),
(472, 'Wood, animal hair, textiles', 1),
(473, 'Yams, frozen, vegetables', 1),
(474, 'yams, frozen, vegetables', 1),
(475, 'Yeast, foodstuff', 1),
(476, 'Zinc, zinc articles, metal', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bx_cusdetail`
--

CREATE TABLE IF NOT EXISTS `bx_cusdetail` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `api_key` varchar(100) NOT NULL,
  `customer_code` int(11) NOT NULL,
  `designation` varchar(110) NOT NULL,
  `name` varchar(100) NOT NULL,
  `contact` int(11) NOT NULL,
  `address` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `landmark` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `pan` varchar(100) NOT NULL,
  `tin` varchar(100) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `bx_cusdetail`
--


-- --------------------------------------------------------

--
-- Table structure for table `bx_custom_clearance`
--

CREATE TABLE IF NOT EXISTS `bx_custom_clearance` (
  `clearanceid` int(11) NOT NULL AUTO_INCREMENT,
  `bookid` varchar(100) NOT NULL,
  `customer_code` int(11) NOT NULL,
  `cc_type` varchar(10) NOT NULL,
  `commodity_id` int(11) NOT NULL,
  `gross_weight` float NOT NULL,
  `hscode` int(8) NOT NULL,
  `type_of_shipment` int(11) NOT NULL,
  `stuffing` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `avail_option` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `create_time` varchar(50) NOT NULL,
  PRIMARY KEY (`clearanceid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `bx_custom_clearance`
--

INSERT INTO `bx_custom_clearance` (`clearanceid`, `bookid`, `customer_code`, `cc_type`, `commodity_id`, `gross_weight`, `hscode`, `type_of_shipment`, `stuffing`, `address`, `avail_option`, `status`, `create_time`) VALUES
(1, 'BUXA1711161048115', 5, 'Import', 25, 6, 58448499, 2, 'null', '', 0, 1, '1479359899473'),
(2, 'BUXA1711161253235', 5, 'Import', 4, 9797, 98989898, 2, 'null', '', 0, 1, '1479367410108'),
(3, 'BUXA1711161258545', 5, 'Import', 3, 54, 55558, 2, 'null', '', 0, 1, '1479367743244'),
(4, 'BUXA1711160102085', 5, 'Import', 13, 8, 8, 2, 'null', '', 0, 1, '1479367934685'),
(5, 'BUXA1711160356125', 5, 'Import', 3, 97, 98, 2, 'null', '', 0, 1, '1479378379379'),
(6, 'BUXA1811160652225', 5, 'Import', 390, 5, 68889889, 2, 'null', '', 0, 1, '1479475355228'),
(7, 'BUXA1911160503275', 5, 'Import', 6, 5, 5, 2, 'null', '', 0, 1, '1479555213179'),
(8, 'BUXA1911160504195', 5, 'Import', 4, 8, 6, 2, 'null', '', 0, 1, '1479555266699'),
(9, 'BUXA0112161136005', 5, 'Import', 66, 87, 8797, 2, 'null', '', 0, 1, '1480572368675'),
(10, 'BUXA0112161242155', 5, 'Import', 47, 989, 96868688, 2, 'null', '', 0, 1, '1480576344442');

-- --------------------------------------------------------

--
-- Table structure for table `bx_freight_forwarding`
--

CREATE TABLE IF NOT EXISTS `bx_freight_forwarding` (
  `freightid` int(11) NOT NULL AUTO_INCREMENT,
  `bookid` varchar(100) NOT NULL,
  `customer_code` int(11) NOT NULL,
  `type_of_shipment` int(11) NOT NULL,
  `pol` varchar(100) NOT NULL,
  `pod` varchar(100) NOT NULL,
  `poc` varchar(100) NOT NULL,
  `incoterm` varchar(50) NOT NULL,
  `destideliveryadr` varchar(100) NOT NULL,
  `measurement` varchar(10) NOT NULL,
  `grossweight` float NOT NULL,
  `packtype` int(11) NOT NULL,
  `noofpack` int(11) NOT NULL,
  `commodity` int(11) NOT NULL,
  `avail_option` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `create_time` varchar(50) NOT NULL,
  PRIMARY KEY (`freightid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `bx_freight_forwarding`
--


-- --------------------------------------------------------

--
-- Table structure for table `bx_icd`
--

CREATE TABLE IF NOT EXISTS `bx_icd` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `icdid` int(11) NOT NULL,
  `icdname` varchar(100) NOT NULL,
  `location` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=47 ;

--
-- Dumping data for table `bx_icd`
--

INSERT INTO `bx_icd` (`id`, `icdid`, `icdname`, `location`, `state`, `status`) VALUES
(1, 1, 'Tughlakabad(TKD)', 'New Delhi', 'Haryana', 1),
(2, 1, 'Sanathnagar(SNF)', 'Hyderabad ', 'Andhra Pradesh', 1),
(3, 1, 'Anarpati', 'East Godavari(AP)', 'Andhra Pradesh ', 1),
(4, 1, 'Nagpur', 'Nagpur', 'Maharashtra', 1),
(5, 1, 'Coimbatore', 'Coimbatore', 'Tamil Nadu ', 1),
(6, 1, 'Tondiarpet', 'Chennai', 'Tamil Nadu', 1),
(7, 1, 'Agra', 'Belanganj', 'Uttar pradesh ', 1),
(8, 1, 'Moradabad', 'Moradabad', 'Uttar pradesh', 1),
(9, 1, 'Guntur', 'Guntur', 'Andhra Pradesh ', 1),
(10, 1, 'New Mulund(E) ', 'Mumbai', 'Maharashtra', 1),
(11, 1, 'Whitefield', 'Bangalore', 'Karnataka', 1),
(12, 1, 'Chirala', 'Dist Guntur(AP) ', 'Andhra Pradesh ', 1),
(13, 1, 'Sabarmati', 'Ahmedabad', 'Gujarat', 1),
(14, 1, 'Amingaon', 'Guwahati', 'Assam', 1),
(15, 1, 'Madurai', 'Madurai', 'Tamil Nadu ', 1),
(16, 1, 'Dhandarikalan', 'Ludhiana', 'Punjab', 1),
(17, 1, 'Kanpur', 'Kanpur', 'Uttar Pradesh ', 1),
(18, 1, 'Daulatabad', 'Aurangabad', 'Maharashtra', 1),
(19, 1, 'Cossipore', 'Kolkata', 'West Bengal ', 1),
(20, 1, 'Jodhpur', 'Jodhpur', 'Rajasthan', 1),
(21, 1, 'Kanakpura', 'Jaipur', 'Rajasthan', 1),
(22, 1, 'Miraje', 'Miraje', 'Maharashtra', 1),
(23, 1, 'Balasore', 'Balasore', 'Orissa', 1),
(24, 1, 'Bhusawal', 'Bhusawal', 'Maharashtra', 1),
(25, 1, 'Riwari', 'Haryana', 'Haryana', 1),
(26, 1, 'Dadri(Greater Noida) ', 'Delhi', 'Haryana', 1),
(27, 1, 'Raipur', 'Khapa', 'Madhya Pradesh ', 1),
(28, 1, 'Tirupur', 'Tirupur', 'Tamil Nadu', 1),
(29, 2, 'Mulund(W)', 'Mumbai', 'Maharashtra', 1),
(30, 2, 'Pithampur', 'Indore', 'Madhya Pradesh ', 1),
(31, 2, 'Tuticorin', 'Milavittan', 'Tamil Nadu ', 1),
(32, 2, 'Babarpur', 'Panipat', 'Haryana', 1),
(33, 2, 'Malanpur', 'Gwailior', 'Madhya Pradesh ', 1),
(34, 2, 'Pondicherry', 'Pondicherry', 'Tamil Nadu ', 1),
(35, 3, 'kochi', 'Kochi', 'Kerala', 1),
(36, 3, 'Chincwad', 'Pune', 'Maharashtra', 1),
(37, 3, 'Wadi Bunder ', 'Mumbai', 'Maharashtra', 1),
(38, 3, 'Vadodara', 'Vadorara', 'Gujarat', 1),
(39, 3, 'Jamshedpur', 'Tatanagar', 'Jharkhand', 1),
(40, 3, 'D''Node', 'Navi Mumbai ', 'Maharashtra', 1),
(41, 4, 'Harbour of Madras ', 'Chennai', 'Tamil Nadu ', 1),
(42, 4, 'Kandla', 'Kandla', 'Gujarat', 1),
(43, 4, 'Haldia', 'Kolkata', 'West Bengal ', 1),
(44, 4, 'Shalimar', 'Kolkata', 'West Bengal ', 1),
(45, 4, 'Vizag(Visakhapatanam)', 'Vizag(Visakapatnam)', 'Andhra Pradesh ', 1),
(46, 5, 'Ballabhgarh', 'Uttar Pradesh ', 'Uttar Pradesh', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bx_icdcategory`
--

CREATE TABLE IF NOT EXISTS `bx_icdcategory` (
  `icdid` int(11) NOT NULL AUTO_INCREMENT,
  `icdname` varchar(500) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`icdid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `bx_icdcategory`
--

INSERT INTO `bx_icdcategory` (`icdid`, `icdname`, `status`) VALUES
(1, 'Rail ICDs with CFSs', 1),
(2, 'Road ICDs with CFS', 1),
(3, 'ICD without CFS', 1),
(4, 'Port Side Container Terminal', 1),
(5, 'Empty Park (Rail Linked)', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bx_packagetype`
--

CREATE TABLE IF NOT EXISTS `bx_packagetype` (
  `packagetypeid` int(11) NOT NULL AUTO_INCREMENT,
  `packagename` varchar(100) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`packagetypeid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=42 ;

--
-- Dumping data for table `bx_packagetype`
--

INSERT INTO `bx_packagetype` (`packagetypeid`, `packagename`, `status`) VALUES
(1, 'Packaging & Printing', 1),
(2, 'Adhesive Tape', 1),
(3, 'Air Dunnage Bag', 1),
(4, 'Aluminum Foil', 1),
(5, 'Bottles', 1),
(6, 'Cans', 1),
(7, 'Crates', 1),
(8, 'Drums', 1),
(9, 'Empty Capsules', 1),
(10, 'FIBC Bag', 1),
(11, 'FlexiTank', 1),
(12, 'Flower Sleeve', 1),
(13, 'Foil Containers', 1),
(14, 'Gas Cylinders', 1),
(15, 'Gift Ribbon', 1),
(16, 'Handles', 1),
(17, 'Hot Stamping Foil', 1),
(18, 'Jar', 1),
(19, 'Lids', 1),
(20, 'Mailing Bags', 1),
(21, 'Media Packaging', 1),
(22, 'Other Packaging Materials', 1),
(23, 'Packaging Bags', 1),
(24, 'Packaging Boxes', 1),
(25, 'Packaging Cup', 1),
(26, 'Packaging Label', 1),
(27, 'Packaging Organza Material', 1),
(28, 'Packaging Product Stocks', 1),
(29, 'Packaging Rope', 1),
(30, 'Packaging Tray', 1),
(31, 'Packaging Tube', 1),
(32, 'Pallets', 1),
(33, 'Paper & Paperboard', 1),
(34, 'Pill Storage Cases', 1),
(35, 'Plastic Film', 1),
(36, 'Preforms', 1),
(37, 'Printing Materials', 1),
(38, 'Printing Services', 1),
(39, 'Protective & Cushioning Material', 1),
(40, 'Pulp', 1),
(41, 'Strapping', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bx_shipment_confirmation`
--

CREATE TABLE IF NOT EXISTS `bx_shipment_confirmation` (
  `cnfrmid` int(11) NOT NULL AUTO_INCREMENT,
  `bookid` varchar(100) NOT NULL,
  `enquiry_status` tinyint(11) NOT NULL DEFAULT '1',
  `quotation` varchar(100) NOT NULL,
  `finalrate` int(11) NOT NULL,
  `serviceid` varchar(10) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`cnfrmid`),
  UNIQUE KEY `bookid` (`bookid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `bx_shipment_confirmation`
--

INSERT INTO `bx_shipment_confirmation` (`cnfrmid`, `bookid`, `enquiry_status`, `quotation`, `finalrate`, `serviceid`, `status`) VALUES
(1, 'BUXA1711161048115', 4, '', 0, '2', 1),
(2, 'BUXA1711161253235', 1, '', 0, '2', 1),
(3, 'BUXA1711161258545', 4, '', 0, '2', 1),
(4, 'BUXA1711160102085', 1, '', 0, '2', 1),
(5, 'BUXA1711160356125', 4, '', 0, '2', 1),
(6, 'BUXA1811160652225', 4, '', 0, '2', 1),
(7, 'BUXA1911160503275', 1, '', 0, '2', 1),
(8, 'BUXA1911160504195', 1, '', 0, '2', 1),
(9, 'BUXA0112161136005', 5, '', 0, '2', 1),
(10, 'BUXA0112161242155', 4, '', 0, '2', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bx_ship_type`
--

CREATE TABLE IF NOT EXISTS `bx_ship_type` (
  `tid` int(11) NOT NULL AUTO_INCREMENT,
  `stypename` varchar(100) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `bx_ship_type`
--

INSERT INTO `bx_ship_type` (`tid`, `stypename`, `status`) VALUES
(1, 'Full Container Load', 1),
(2, 'Less than Container Load', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bx_transport`
--

CREATE TABLE IF NOT EXISTS `bx_transport` (
  `trnsid` int(11) NOT NULL AUTO_INCREMENT,
  `customer_code` int(11) NOT NULL,
  `bookid` varchar(100) NOT NULL,
  `commodity_id` int(11) NOT NULL,
  `demlength` int(11) NOT NULL,
  `demwidth` int(11) NOT NULL,
  `demheight` int(11) NOT NULL,
  `shipment_type` int(11) NOT NULL,
  `mesurement` varchar(10) NOT NULL,
  `gross_weight` float NOT NULL,
  `no_of_pack` int(11) NOT NULL,
  `pack_type` int(11) NOT NULL,
  `source` varchar(100) NOT NULL,
  `destination` varchar(100) NOT NULL,
  `avail_option` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `create_time` varchar(50) NOT NULL,
  PRIMARY KEY (`trnsid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `bx_transport`
--


-- --------------------------------------------------------

--
-- Table structure for table `bx_user`
--

CREATE TABLE IF NOT EXISTS `bx_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `api_key` varchar(100) NOT NULL,
  `fcm_id` longtext,
  `company` varchar(100) NOT NULL,
  `uname` varchar(100) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `create_time` varchar(20) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `bx_user`
--

INSERT INTO `bx_user` (`uid`, `api_key`, `fcm_id`, `company`, `uname`, `mobile`, `email`, `password`, `create_time`, `status`) VALUES
(5, '6c13b559a3224bcba3a9e1a52b43c0d4', 'fx5uuUqxyVk:APA91bFaTHij8hJg8DSVtLM-DJ_iuPvc6V9dEd65oZAvhEr4Y0Zx1b95wALhMQz1QTCiOKeps-l7_a3-cq2J1jgCW4Mbr_clPtqmUuxIBSRfBv4t3B8dKaUTmekNuRCCVs7d6Kao3bDl', 'ADM', 'Yogesh Pimpalkar', '8466525807', 'yogesh.blueoort@gmail.com', 'test', '1473837432596', 1),
(6, 'e2768f947253e676a59b52e236020955', 'cpCTmhnD9vo:APA91bGAMAJAJCxuQQJJjqTywVHPxjRW3EYRGz5z28dGemNc5rheS55cwMTWeQwnCWIwY86l3uenplUBnxRfIAlAroX3qE7gglpuJuWdPIos1P7zB5voeKUaW6CaaPbqZuQFd79G1vdL', 'Yogesh', 'Vz', '259794464', 'test@test.com', 'test', '1480398844925', 1);

-- --------------------------------------------------------

--
-- Table structure for table `db_transport_service`
--

CREATE TABLE IF NOT EXISTS `db_transport_service` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `servicename` varchar(50) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `db_transport_service`
--

INSERT INTO `db_transport_service` (`service_id`, `servicename`, `status`) VALUES
(1, 'Transportation', 1),
(2, 'Custom Clearance', 1),
(3, 'Freight Forwarding', 1);

-- --------------------------------------------------------

--
-- Table structure for table `db_transport_type`
--

CREATE TABLE IF NOT EXISTS `db_transport_type` (
  `trid` int(11) NOT NULL AUTO_INCREMENT,
  `tname` varchar(50) NOT NULL,
  `active_status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`trid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `db_transport_type`
--

INSERT INTO `db_transport_type` (`trid`, `tname`, `active_status`) VALUES
(1, 'Ocean', 1),
(2, 'Trucking', 0),
(3, 'Air', 0),
(4, 'Rail', 0),
(5, 'Courier', 0);
