-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 22, 2016 at 02:47 PM
-- Server version: 5.6.21
-- PHP Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `buxa_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `bx_commodity`
--

CREATE TABLE IF NOT EXISTS `bx_commodity` (
`comid` int(11) NOT NULL,
  `comodity` longtext NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=477 DEFAULT CHARSET=latin1;

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
`cid` int(11) NOT NULL,
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
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bx_custom_clearance`
--

CREATE TABLE IF NOT EXISTS `bx_custom_clearance` (
`clearanceid` int(11) NOT NULL,
  `bookid` varchar(100) NOT NULL,
  `customer_code` int(11) NOT NULL,
  `type_of_shipment` int(11) NOT NULL,
  `stuffing` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `avail_option` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `create_time` varchar(50) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bx_custom_clearance`
--

INSERT INTO `bx_custom_clearance` (`clearanceid`, `bookid`, `customer_code`, `type_of_shipment`, `stuffing`, `address`, `avail_option`, `status`, `create_time`) VALUES
(1, 'BUXA2209160308355', 5, 2, 'Vendor CFS', 'Thane West, Thane, Maharashtra, India', 0, 1, '1474537255796'),
(2, 'BUXA2209160342325', 5, 1, 'Dock stuffing', 'Forum Bus Stop, Hosur Road, 7th Block, Bengaluru, Karnataka, India', 0, 1, '1474539169775'),
(3, 'BUXA2209160351595', 5, 1, 'Factory stuffing', 'Thane West, Thane, Maharashtra, India', 0, 1, '1474539728625'),
(4, 'BUXA2209160608265', 0, 1, 'Dock stuffing', 'Yercaud, Tamil Nadu, India', 1, 1, '1474548235317');

-- --------------------------------------------------------

--
-- Table structure for table `bx_freight_forwarding`
--

CREATE TABLE IF NOT EXISTS `bx_freight_forwarding` (
`freightid` int(11) NOT NULL,
  `bookid` varchar(100) NOT NULL,
  `customer_code` int(11) NOT NULL,
  `type_of_shipment` int(11) NOT NULL,
  `pol` varchar(100) NOT NULL,
  `pod` varchar(100) NOT NULL,
  `avail_option` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `create_time` varchar(50) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bx_freight_forwarding`
--

INSERT INTO `bx_freight_forwarding` (`freightid`, `bookid`, `customer_code`, `type_of_shipment`, `pol`, `pod`, `avail_option`, `status`, `create_time`) VALUES
(1, 'BUXA2209160519355', 5, 1, 'Yudhister Setu, Shastri Park, New Delhi, Delhi, India', 'Tirupati, Andhra Pradesh, India', 0, 1, '1474544998177'),
(2, 'BUXA2209160608265', 5, 1, 'Raipur, Chhattisgarh, India', 'Thane West, Thane, Maharashtra, India', 0, 1, '1474548048759');

-- --------------------------------------------------------

--
-- Table structure for table `bx_icd`
--

CREATE TABLE IF NOT EXISTS `bx_icd` (
`id` int(11) NOT NULL,
  `icdid` int(11) NOT NULL,
  `icdname` varchar(100) NOT NULL,
  `location` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=latin1;

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
`icdid` int(11) NOT NULL,
  `icdname` varchar(500) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

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
`packagetypeid` int(11) NOT NULL,
  `packagename` varchar(100) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;

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
`cnfrmid` int(11) NOT NULL,
  `bookid` varchar(100) NOT NULL,
  `finalrate` int(11) NOT NULL,
  `serviceid` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bx_ship_type`
--

CREATE TABLE IF NOT EXISTS `bx_ship_type` (
`tid` int(11) NOT NULL,
  `stypename` varchar(100) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

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
`trnsid` int(11) NOT NULL,
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
  `lrcopy` varchar(500) NOT NULL,
  `avail_option` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `create_time` varchar(50) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bx_transport`
--

INSERT INTO `bx_transport` (`trnsid`, `customer_code`, `bookid`, `commodity_id`, `demlength`, `demwidth`, `demheight`, `shipment_type`, `mesurement`, `gross_weight`, `no_of_pack`, `pack_type`, `source`, `destination`, `lrcopy`, `avail_option`, `status`, `create_time`) VALUES
(1, 5, 'BUXA2109160543395', 76, 5, 3, 63, 2, '50', 500, 9, 15, 'Thane, Maharashtra, India', 'FDA Bhawan, Kotla Marg, New Delhi, Delhi, India', '', 0, 1, '1474460303903'),
(2, 5, 'BUXA2109160644475', 56, 5, 6, 9, 2, '50', 500, 25, 26, 'Nhava Sheva, Navi Mumbai, Maharashtra, India', 'Ratnagiri, Chiplun, Maharashtra, India', '', 0, 1, '1474463745266'),
(3, 5, 'BUXA2109160648155', 76, 2, 6, 3, 1, '40', 1, 2, 14, 'Frazer Town, Bengaluru, Karnataka, India', 'Thane, Maharashtra, India', '', 0, 1, '1474464070221'),
(4, 0, 'BUXA2209160351595', 56, 5, 6, 2, 1, '40', 5, 2, 30, 'Ghaziabad, Uttar Pradesh, India', 'Yelahanka, Bengaluru, Karnataka, India', '', 1, 1, '1474539795220'),
(5, 0, 'BUXA2209160608265', 56, 5, 9, 6, 1, '40', 25, 25, 17, 'JGI Jain University Jain College, Bengaluru, Karnataka, India', 'Gdh Securities Pvt Ltd, Hyderabad, Telangana, Indiag', '', 1, 1, '1474548220963');

-- --------------------------------------------------------

--
-- Table structure for table `bx_user`
--

CREATE TABLE IF NOT EXISTS `bx_user` (
`uid` int(11) NOT NULL,
  `api_key` varchar(100) NOT NULL,
  `gcm_id` longtext,
  `company` varchar(100) NOT NULL,
  `uname` varchar(100) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `create_time` varchar(20) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bx_user`
--

INSERT INTO `bx_user` (`uid`, `api_key`, `gcm_id`, `company`, `uname`, `mobile`, `email`, `password`, `create_time`, `status`) VALUES
(5, '6c13b559a3224bcba3a9e1a52b43c0d4', NULL, 'ADM', 'Yogesh Pimpalkar', '8466525807', 'yogesh@adm.com', 'test', '1473837432596', 1);

-- --------------------------------------------------------

--
-- Table structure for table `db_transport_service`
--

CREATE TABLE IF NOT EXISTS `db_transport_service` (
`service_id` int(11) NOT NULL,
  `servicename` varchar(50) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

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
`trid` int(11) NOT NULL,
  `tname` varchar(50) NOT NULL,
  `active_status` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `db_transport_type`
--

INSERT INTO `db_transport_type` (`trid`, `tname`, `active_status`) VALUES
(1, 'Ocean', 1),
(2, 'Trucking', 0),
(3, 'Air', 0),
(4, 'Rail', 0),
(5, 'Courier', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bx_commodity`
--
ALTER TABLE `bx_commodity`
 ADD PRIMARY KEY (`comid`);

--
-- Indexes for table `bx_cusdetail`
--
ALTER TABLE `bx_cusdetail`
 ADD PRIMARY KEY (`cid`);

--
-- Indexes for table `bx_custom_clearance`
--
ALTER TABLE `bx_custom_clearance`
 ADD PRIMARY KEY (`clearanceid`);

--
-- Indexes for table `bx_freight_forwarding`
--
ALTER TABLE `bx_freight_forwarding`
 ADD PRIMARY KEY (`freightid`);

--
-- Indexes for table `bx_icd`
--
ALTER TABLE `bx_icd`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bx_icdcategory`
--
ALTER TABLE `bx_icdcategory`
 ADD PRIMARY KEY (`icdid`);

--
-- Indexes for table `bx_packagetype`
--
ALTER TABLE `bx_packagetype`
 ADD PRIMARY KEY (`packagetypeid`);

--
-- Indexes for table `bx_shipment_confirmation`
--
ALTER TABLE `bx_shipment_confirmation`
 ADD PRIMARY KEY (`cnfrmid`);

--
-- Indexes for table `bx_ship_type`
--
ALTER TABLE `bx_ship_type`
 ADD PRIMARY KEY (`tid`);

--
-- Indexes for table `bx_transport`
--
ALTER TABLE `bx_transport`
 ADD PRIMARY KEY (`trnsid`);

--
-- Indexes for table `bx_user`
--
ALTER TABLE `bx_user`
 ADD PRIMARY KEY (`uid`);

--
-- Indexes for table `db_transport_service`
--
ALTER TABLE `db_transport_service`
 ADD PRIMARY KEY (`service_id`);

--
-- Indexes for table `db_transport_type`
--
ALTER TABLE `db_transport_type`
 ADD PRIMARY KEY (`trid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bx_commodity`
--
ALTER TABLE `bx_commodity`
MODIFY `comid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=477;
--
-- AUTO_INCREMENT for table `bx_cusdetail`
--
ALTER TABLE `bx_cusdetail`
MODIFY `cid` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `bx_custom_clearance`
--
ALTER TABLE `bx_custom_clearance`
MODIFY `clearanceid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `bx_freight_forwarding`
--
ALTER TABLE `bx_freight_forwarding`
MODIFY `freightid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `bx_icd`
--
ALTER TABLE `bx_icd`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=47;
--
-- AUTO_INCREMENT for table `bx_icdcategory`
--
ALTER TABLE `bx_icdcategory`
MODIFY `icdid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `bx_packagetype`
--
ALTER TABLE `bx_packagetype`
MODIFY `packagetypeid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=42;
--
-- AUTO_INCREMENT for table `bx_shipment_confirmation`
--
ALTER TABLE `bx_shipment_confirmation`
MODIFY `cnfrmid` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `bx_ship_type`
--
ALTER TABLE `bx_ship_type`
MODIFY `tid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `bx_transport`
--
ALTER TABLE `bx_transport`
MODIFY `trnsid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `bx_user`
--
ALTER TABLE `bx_user`
MODIFY `uid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `db_transport_service`
--
ALTER TABLE `db_transport_service`
MODIFY `service_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `db_transport_type`
--
ALTER TABLE `db_transport_type`
MODIFY `trid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
