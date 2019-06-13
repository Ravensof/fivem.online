package online.fivem.common.gtav

// https://www.se7ensins.com/forums/threads/ref-gta-v-vehicle-hashes-list-2.1615439/
// ^([A-Z0-9]+) 0x[^ ]+ [0-9]+ (-?[0-9]+) ([A-z0-9]+) (.*) ("[^"]+"|-|[0-9]+)$
// $1($2,$3,$5), //$4

enum class NativeVehicle(val hash: Int, category: Category, defaultPrice: Int) {
	//NAME HEX uint32 int32 CATEGORY DESC UPDATE NAME HEX2 NAME HEX uint32 int32 CATEGORY DESC UPDATE NAME PRICE1 PRICE2 HIGH_PRICE
	BLIMP3(-307958377, Category.AFTER, 1_190_350),
	FREECRAWLER(-54332285, Category.AFTER, 597_000),
	MENACER(2044532910, Category.AFTER, 1_775_000),
	MULE4(1945374990, Category.AFTER, 95_700),
	OPPRESSOR2(2069146067, Category.AFTER, 3_890_250),
	PATRIOT2(-420911112, Category.AFTER, 611_800),
	PBUS2(345756458, Category.AFTER, 1_385_000),
	POUNDER2(1653666139, Category.AFTER, 820_550),
	SCRAMJET(-638562243, Category.AFTER, 4_628_400),
	SPEEDO4(219613597, Category.AFTER, Int.MAX_VALUE),
	STAFFORD(321186144, Category.AFTER, 1_272_000),
	STRIKEFORCE(1692272545, Category.AFTER, 3_800_000),
	SWINGER(500482303, Category.AFTER, 909_000),
	TERBYTE(-1988428699, Category.AFTER, 3_459_500),

	//NAME HEX uint32 int32 CATEGORY DESC UPDATE NAME PRICE1 PRICE2 HIGH_PRICE
	ADDER(-1216765807, Category.Super, 1_000_000),
	AIRBUS(1283517198, Category.Service, 550_000), //(Airport Bus)
	AIRTUG(1560980623, Category.Service, Int.MAX_VALUE),
	AKULA(1181327175, Category.Helicopter, 3_704_050),
	AKUMA(1672195559, Category.Motorcycle, 9_000),
	ALPHA(767087018, Category.Sport, 150_000),
	ALPHAZ1(-1523619738, Category.Plane, 2_121_350),
	AMBULANCE(1171614426, Category.Emergency, Int.MAX_VALUE),
	ANNIHILATOR(837858166, Category.Helicopter, 1_825_000),
	APC(562680400, Category.Military, 2_325_000),
	ARDENT(159274291, Category.SportsClassic, 1_150_000),
	ARMYTANKER(-1207431159, Category.Trailer, Int.MAX_VALUE),
	ARMYTRAILER(-1476447243, Category.Trailer, Int.MAX_VALUE), //(Army Flatbed Trailer)
	ARMYTRAILER2(-1637149482, Category.Trailer, Int.MAX_VALUE), //(Flatbed With Cutter Trailer)
	ASEA(-1809822327, Category.Sedan, 12_000),
	ASEA2(-1807623979, Category.Sedan, Int.MAX_VALUE), //(Snowy Asea)
	ASTEROPE(-1903012613, Category.Sedan, 26_000),
	AUTARCH(-313185164, Category.Super, 1_955_000),
	AVARUS(-2115793025, Category.Motorcycle, 116_000),
	AVENGER(-2118308144, Category.Plane, 3_450_000),
	AVENGER2(408970549, Category.Plane, 3_450_000),
	BAGGER(-2140431165, Category.Motorcycle, 16_000),
	BALETRAILER(-399841706, Category.Trailer, Int.MAX_VALUE),
	BALLER(-808831384, Category.SUV, 90_000),
	BALLER2(142944341, Category.SUV, 90_000), //(RangeRover Evoque)
	BALLER3(1878062887, Category.SUV, 374_000), //(Baller LE)
	BALLER4(634118882, Category.SUV, 247_000), //(Baller LE LWB)
	BALLER5(470404958, Category.SUV, 374_000), //(Baller LE (Armored)
	BALLER6(666166960, Category.SUV, 513_000), //(Baller LE LWB (Armored)
	BANSHEE(-1041692462, Category.Sport, 105_000),
	BANSHEE2(633712403, Category.Super, 670_000), //(Banshee 900R)
	BARRACKS(-823509173, Category.Emergency, 450_000), //(Barracks With Backcover)
	BARRACKS2(1074326203, Category.Emergency, Int.MAX_VALUE), //(Barracks Semi)
	BARRACKS3(630371791, Category.Emergency, Int.MAX_VALUE), //(Dark Camo and New Cover)
	BARRAGE(-212993243, Category.Military, 2_121_350),
	BATI(-114291515, Category.Motorcycle, 15_000),
	BATI2(-891462355, Category.Motorcycle, 15_000), //(Bati Livery)
	BENSON(2053223216, Category.Truck, Int.MAX_VALUE),
	BESRA(1824333165, Category.Plane, 1_150_000),
	BESTIAGTS(1274868363, Category.Sport, 610_000),
	BF400(86520421, Category.Motorcycle, 95_000),
	BFINJECTION(1126868326, Category.Offroad, 16_000),
	BIFF(850991848, Category.Truck, Int.MAX_VALUE),
	BIFTA(-349601129, Category.Offroad, 75_000),
	BISON(-16948145, Category.Pickup, 30_000),
	BISON2(2072156101, Category.Pickup, 30_000), //(Cowboy Construction Bison)
	BISON3(1739845664, Category.Pickup, 30_000), //(Landscapeing Bison)
	BJXL(850565707, Category.SUV, 27_000),
	BLADE(-1205801634, Category.Muscle, 160_000),
	BLAZER(-2128233223, Category.Offroad, 8_000),
	BLAZER2(-48031959, Category.Offroad, 8_000), //(Lifeguard Blazer)
	BLAZER3(-1269889662, Category.Offroad, 69_000), //(Trevor's Hotrod Blazer)
	BLAZER4(-440768424, Category.Offroad, 81_000), //(Street Blazer)
	BLAZER5(-1590337689, Category.Offroad, 1_755_600), //(Blazer Aqua)
	BLIMP(-150975354, Category.Helicopter, Int.MAX_VALUE),
	BLIMP2(-613725916, Category.Helicopter, Int.MAX_VALUE), //(Xero Blimp)
	BLISTA(-344943009, Category.Compact, 16_000),
	BLISTA2(1039032026, Category.Sport, 42_000), //(Blista Compact)
	BLISTA3(-591651781, Category.Sport, Int.MAX_VALUE), //(Go Go Monkey Blista)
	BMX(1131912276, Category.Bicycle, 800),
	BOATTRAILER(524108981, Category.Trailer, Int.MAX_VALUE), //(Boat Trailer)
	BOBCATXL(1069929536, Category.Pickup, 23_000),
	BODHI2(-1435919434, Category.Offroad, 25_000), //(Trevor's Truck)
	BOMBUSHKA(-32878452, Category.Plane, 5_918_500),
	BOXVILLE(-1987130134, Category.Truck, 25_000), //(Water&Power Boxville)
	BOXVILLE2(-233098306, Category.Truck, 25_000), //(Postal Boxville)
	BOXVILLE3(121658888, Category.Truck, 25_000), //(Humane Boxville)
	BOXVILLE4(444171386, Category.Truck, 59_850), //(Post OP)
	BOXVILLE5(682434785, Category.Truck, 2_926_000), //(Armored Boxville)
	BRAWLER(-1479664699, Category.Offroad, 715_000),
	BRICKADE(-305727417, Category.Service, 1_110_000),
	BRIOSO(1549126457, Category.Compact, 155_000), //(Brioso R/A)
	BTYPE(117401876, Category.SportsClassic, 1_150_000), //(Roosevelt)
	BTYPE2(-831834716, Category.Muscle, 550_000), //(Fränken Stange)
	BTYPE3(-602287871, Category.SportsClassic, 982_000), //(Roosevelt Valor)
	BUCCANEER(-682211828, Category.Muscle, 29_000),
	BUCCANEER2(-1013450936, Category.Muscle, 419_000), //(Buccaneer Custom)
	BUFFALO(-304802106, Category.Sport, 35_000),
	BUFFALO2(736902334, Category.Sport, 96_000), //(Franklin's Buffalo)
	BUFFALO3(237764926, Category.Sport, 535_000), //(Sprunk Buffalo)
	BULLDOZER(1886712733, Category.Service, Int.MAX_VALUE),
	BULLET(-1696146015, Category.Super, 155_000),
	BURRITO(-1346687836, Category.Van, 13_000), //(Cowboy Cons/Water&Power)
	BURRITO2(-907477130, Category.Van, 13_000), //(Bugstars Burrito)
	BURRITO3(-1743316013, Category.Van, 13_000), //(No livery Burrito)
	BURRITO4(893081117, Category.Van, 13_000), //(Cowboy Construction Burrito)
	BURRITO5(1132262048, Category.Van, 13_000), //(Snowy Burrito)
	BUS(-713569950, Category.Service, 500_000),
	BUZZARD(788747387, Category.Helicopter, 2_000_000),
	BUZZARD2(745926877, Category.Helicopter, Int.MAX_VALUE), //(Gunless / Transport Buzzard)
	CABLECAR(-960289747, Category.Service, Int.MAX_VALUE),
	CADDY(1147287684, Category.Service, Int.MAX_VALUE), //(Prolaps Caddy)
	CADDY2(-537896628, Category.Service, 85_000), //(Old Caddy)
	CADDY3(-769147461, Category.Utility, 120_000),
	CAMPER(1876516712, Category.Van, Int.MAX_VALUE),
	CARACARA(1254014755, Category.Offroad, 1_775_000),
	CARBONIZZARE(2072687711, Category.Sport, 195_000),
	CARBONRS(11251904, Category.Motorcycle, 40_000),
	CARGOBOB(-50547061, Category.Helicopter, 1_790_000),
	CARGOBOB2(1621617168, Category.Helicopter, 1_995_000), //(Medical Cargobob)
	CARGOBOB3(1394036463, Category.Helicopter, Int.MAX_VALUE), //(Trevor's Cargobob)
	CARGOBOB4(2025593404, Category.Helicopter, Int.MAX_VALUE), //(Yacht Cargobob)
	CARGOPLANE(368211810, Category.Plane, Int.MAX_VALUE),
	CASCO(941800958, Category.SportsClassic, 904_400),
	CAVALCADE(2006918058, Category.SUV, 60_000), //(Escalade 2005)
	CAVALCADE2(-789894171, Category.SUV, 60_000), //(Escalade 2013)
	CHEBUREK(-988501280, Category.SportsClassic, 145_000),
	CHEETAH(-1311154784, Category.Super, 650_000),
	CHEETAH2(223240013, Category.SportsClassic, 865_000),
	CHERNOBOG(-692292317, Category.Military, 3_311_700),
	CHIMERA(6774487, Category.Motorcycle, 210_000),
	CHINO(349605904, Category.Muscle, 225_000),
	CHINO2(-1361687965, Category.Muscle, 410_000), //(Chino Custom)
	CLIFFHANGER(390201602, Category.Motorcycle, 225_000),
	COACH(-2072933068, Category.Service, 525_000), //(Dashound)
	COG55(906642318, Category.Sedan, 154_000), //(Cognoscenti 55)
	COG552(704435172, Category.Sedan, 396_000), //(Cognoscenti 55 (Armored)
	COGCABRIO(330661258, Category.Coupe, 185_000),
	COGNOSCENTI(-2030171296, Category.Sedan, 254_000),
	COGNOSCENTI2(-604842630, Category.Sedan, 558_000), //(Cognoscenti (Armored)
	COMET2(-1045541610, Category.Sport, 100_000),
	COMET3(-2022483795, Category.Sport, 745_000), //(Comet Retro Custom)
	COMET4(1561920505, Category.Sport, 710_000),
	COMET5(661493923, Category.Sport, 1_145_000),
	CONTENDER(683047626, Category.Pickup, 250_000),
	COQUETTE(108773431, Category.Sport, 138_000),
	COQUETTE2(1011753235, Category.SportsClassic, 665_000), //(Coquette Classic)
	COQUETTE3(784565758, Category.SportsClassic, 695_000), //(Blackfin)
	CRUISER(448402357, Category.Bicycle, 800),
	CRUSADER(321739290, Category.Emergency, 225_000),
	CUBAN800(-644710429, Category.Plane, 240_000),
	CUTTER(-1006919392, Category.Truck, Int.MAX_VALUE),
	CYCLONE(1392481335, Category.Super, 1_890_000),
	DAEMON(2006142190, Category.Motorcycle, 20_000),
	DAEMON2(-1404136503, Category.Motorcycle, 145_000),
	DEFILER(822018448, Category.Motorcycle, 412_000),
	DELUXO(1483171323, Category.SportsClassic, 4_721_500),
	DIABLOUS(-239841468, Category.Motorcycle, 169_000),
	DIABLOUS2(1790834270, Category.Motorcycle, 414_000), //(Diabolus Custom)
	DILETTANTE(-1130810103, Category.Compact, 25_000),
	DILETTANTE2(1682114128, Category.Compact, 25_000), //(Merryweather Patrol Car)
	DINGHY(1033245328, Category.Boat, Int.MAX_VALUE),
	DINGHY2(276773164, Category.Boat, Int.MAX_VALUE), //(2-Seater)
	DINGHY3(509498602, Category.Boat, 166_250), //(New Map On Dash)
	DINGHY4(867467158, Category.Boat, Int.MAX_VALUE), //(Yacht Dinghy)
	DLOADER(1770332643, Category.Offroad, 15_000),
	DOCKTRAILER(-2140210194, Category.Trailer, Int.MAX_VALUE), //(Shipping Container Trailer)
	DOCKTUG(-884690486, Category.Truck, Int.MAX_VALUE),
	DODO(-901163259, Category.Plane, 500_000),
	DOMINATOR(80636076, Category.Muscle, 35_000),
	DOMINATOR2(-915704871, Category.Muscle, 315_000), //(Pisswasser Dominator)
	DOMINATOR3(-986944621, Category.Muscle, 725_000),
	DOUBLE(-1670998136, Category.Motorcycle, 12_000),
	DUBSTA(1177543287, Category.SUV, 70_000),
	DUBSTA2(-394074634, Category.SUV, 70_000), //(Blacked Out Dubsta)
	DUBSTA3(-1237253773, Category.Offroad, 249_000), //(Dubsta 6x6)
	DUKES(723973206, Category.Muscle, 62_000),
	DUKES2(-326143852, Category.Muscle, 665_000), //(Duke O'Death)
	DUMP(-2130482718, Category.Truck, 1_000_000),
	DUNE(-1661854193, Category.Offroad, 20_000),
	DUNE2(534258863, Category.Offroad, 1_000_000), //(Spacedocker)
	DUNE3(1897744184, Category.Offroad, 850_000),
	DUNE4(-827162039, Category.Offroad, Int.MAX_VALUE), //(Ramp Buggy Custom)
	DUNE5(-312295511, Category.Offroad, 3_192_000), //(Ramp Buggy)
	DUSTER(970356638, Category.Plane, 275_000),
	ELEGY(196747873, Category.Sport, 904_000),
	ELEGY2(-566387422, Category.Sport, 95_000),
	ELLIE(-1267543371, Category.Muscle, 565_000),
	EMPEROR(-685276541, Category.Sedan, 8_000),
	EMPEROR2(-1883002148, Category.Sedan, 5_000), //(Rusty Emperor)
	EMPEROR3(-1241712818, Category.Sedan, 5_000), //(Snowy Emperor)
	ENDURO(1753414259, Category.Motorcycle, 48_000),
	ENTITY2(-2120700196, Category.Super, 2_305_000),
	ENTITYXF(-1291952903, Category.Super, 795_000),
	ESSKEY(2035069708, Category.Motorcycle, 264_000),
	EXEMPLAR(-5153954, Category.Coupe, 205_000),
	F620(-591610296, Category.Coupe, 80_000),
	FACTION(-2119578145, Category.Coupe, 36_000),
	FACTION2(-1790546981, Category.Coupe, 371_000), //(Faction Custom)
	FACTION3(-2039755226, Category.Muscle, 731_000), //(Faction Custom Donk)
	FAGALOA(1617472902, Category.SportsClassic, 335_000),
	FAGGIO(-1842748181, Category.Motorcycle, 47_500),
	FAGGIO2(55628203, Category.Motorcycle, 5_000),
	FAGGIO3(-1289178744, Category.Motorcycle, 55_000), //(Faggio Mod)
	FAGGION(2134395284, Category.Motorcycle, Int.MAX_VALUE),
	FBI(1127131465, Category.Emergency, Int.MAX_VALUE), //(FIB Buffalo)
	FBI2(-1647941228, Category.Emergency, Int.MAX_VALUE), //(FIB Granger)
	FCR(627535535, Category.Motorcycle, 135_000), //(FCR 1000)
	FCR2(-757735410, Category.Motorcycle, 331_000), //(FCR 1000 Custom)
	FELON(-391594584, Category.Coupe, 90_000),
	FELON2(-89291282, Category.Coupe, 95_000), //(Felon GT Convertible)
	FELTZER2(-1995326987, Category.Sport, 145_000),
	FELTZER3(-1566741232, Category.SportsClassic, 975_000), //(Stirling GT)
	FIRETRUK(1938952078, Category.Emergency, Int.MAX_VALUE),
	FIXTER(-836512833, Category.Bicycle, Int.MAX_VALUE),
	FLASHGT(-1259134696, Category.Sport, 1_675_000),
	FLATBED(1353720154, Category.Truck, Int.MAX_VALUE),
	FMJ(1426219628, Category.Super, 1_750_000),
	FORKLIFT(1491375716, Category.Service, Int.MAX_VALUE),
	FQ2(-1137532101, Category.SUV, 50_000),
	FREIGHT(1030400667, Category.Train, Int.MAX_VALUE), //(Freight Train)
	FREIGHTCAR(184361638, Category.Train, Int.MAX_VALUE), //(Train Well Car)
	FREIGHTCONT1(920453016, Category.Train, Int.MAX_VALUE), //(Train Container)
	FREIGHTCONT2(240201337, Category.Train, Int.MAX_VALUE), //(Train Container Livery)
	FREIGHTGRAIN(642617954, Category.Train, Int.MAX_VALUE), //(Train Boxcar)
	FREIGHTTRAILER(-777275802, Category.Trailer, Int.MAX_VALUE), //(Freight Train Flatbed)
	FROGGER(744705981, Category.Helicopter, 1_300_000),
	FROGGER2(1949211328, Category.Helicopter, Int.MAX_VALUE), //(Trevor's Frogger)
	FUGITIVE(1909141499, Category.Sedan, 24_000),
	FUROREGT(-1089039904, Category.Sport, 448_000),
	FUSILADE(499169875, Category.Sport, 36_000),
	FUTO(2016857647, Category.Sport, 9_000),
	GARGOYLE(741090084, Category.Motorcycle, 120_000),
	GAUNTLET(-1800170043, Category.Muscle, 32_000),
	GAUNTLET2(349315417, Category.Muscle, 230_000), //(Redwood Gauntlet)
	GB200(1909189272, Category.Sport, 940_000),
	GBURRITO(-1745203402, Category.Van, 16_000),
	GBURRITO2(296357396, Category.Van, 86_450), //(No Livery)
	GLENDALE(75131841, Category.Sedan, 200_000),
	GP1(1234311532, Category.Super, 1_260_000),
	GRAINTRAILER(1019737494, Category.Trailer, Int.MAX_VALUE),
	GRANGER(-1775728740, Category.SUV, 35_000),
	GRESLEY(-1543762099, Category.SUV, 29_000),
	GT500(-2079788230, Category.SportsClassic, 785_000),
	GUARDIAN(-2107990196, Category.Offroad, 375_000),
	HABANERO(884422927, Category.SUV, 42_000),
	HAKUCHOU(1265391242, Category.Motorcycle, 82_000),
	HAKUCHOU2(-255678177, Category.Motorcycle, 976_000), //(Hakuchou Drag)
	HALFTRACK(-32236122, Category.Military, 1_695_000),
	HANDLER(444583674, Category.Truck, Int.MAX_VALUE),
	HAULER(1518533038, Category.Truck, Int.MAX_VALUE),
	HAULER2(387748548, Category.Commercial, 1_400_000),
	HAVOK(-1984275979, Category.Helicopter, 2_300_900),
	HERMES(15219735, Category.Muscle, 535_000),
	HEXER(301427732, Category.Motorcycle, 15_000),
	HOTKNIFE(37348240, Category.Muscle, 90_000),
	HOTRING(1115909093, Category.Sport, 830_000),
	HOWARD(-1007528109, Category.Plane, 1_296_750),
	HUNTER(-42959138, Category.Helicopter, 4_123_000),
	HUNTLEY(486987393, Category.SUV, 195_000),
	HUSTLER(600450546, Category.Muscle, 625_000),
	HYDRA(970385471, Category.Plane, 3_990_000),
	INFERNUS(418536135, Category.Super, 440_000),
	INFERNUS2(-1405937764, Category.Sport, 915_000), //(Infernus Classic)
	INGOT(-1289722222, Category.Sedan, 9_000),
	INNOVATION(-159126838, Category.Motorcycle, 92_500),
	INSURGENT(-1860900134, Category.Offroad, 1_795_500), //(Mounted Gun)
	INSURGENT2(2071877360, Category.Offroad, 897_750), //(Transport)
	INSURGENT3(-1924433270, Category.Offroad, 1_998_000),
	INTRUDER(886934177, Category.Sedan, 16_000),
	ISSI2(-1177863319, Category.Compact, 18_000), //(Issi Convertible)
	ISSI3(931280609, Category.Compact, 360_000),
	ITALIGTB(-2048333973, Category.Super, 1_189_000),
	ITALIGTB2(-482719877, Category.Super, 1_684_000), //(Itali GTB Custom)
	JACKAL(-624529134, Category.Coupe, 60_000),
	JB700(1051415893, Category.SportsClassic, 350_000),
	JESTER(-1297672541, Category.Sport, 240_000),
	JESTER2(-1106353882, Category.Sport, 350_000), //(Rester Race)
	JESTER3(-214906006, Category.SportsClassic, 790_000),
	JET(1058115860, Category.Plane, Int.MAX_VALUE),
	JETMAX(861409633, Category.Boat, 299_000),
	JOURNEY(-120287622, Category.Van, 15_000),
	KALAHARI(92612664, Category.Offroad, 40_000),
	KAMACHO(-121446169, Category.Offroad, 345_000),
	KHAMELION(544021352, Category.Sport, 100_000),
	KHANJALI(-1435527158, Category.Military, 3_850_350),
	KURUMA(-1372848492, Category.Sport, 126_350),
	KURUMA2(410882957, Category.Sport, 698_250), //(Armored)
	LANDSTALKER(1269098716, Category.SUV, 58_000),
	LAZER(-1281684762, Category.Plane, 6_500_000),
	LE7B(-1232836011, Category.Super, 2_475_000), //(RE-7B)
	LECTRO(640818791, Category.Motorcycle, 997_500),
	LGUARD(469291905, Category.Emergency, 35_000),
	LIMO2(-114627507, Category.Sedan, 1_650_000), //(Turreted Limo)
	LURCHER(2068293287, Category.SportsClassic, 650_000), //(Zombie Hearse)
	LUXOR(621481054, Category.Plane, 1_625_000),
	LUXOR2(-1214293858, Category.Plane, 10_000_000), //(Gold)
	LYNX(482197771, Category.Sport, 1_735_000),
	MAMBA(-1660945322, Category.Sport, 995_000),
	MAMMATUS(-1746576111, Category.Plane, 300_000),
	MANANA(-2124201592, Category.SportsClassic, 8_000),
	MANCHEZ(-1523428744, Category.Motorcycle, 67_000),
	MARQUIS(-1043459709, Category.Boat, 413_990),
	MARSHALL(1233534620, Category.Offroad, 500_000),
	MASSACRO(-142942670, Category.Sport, 275_000),
	MASSACRO2(-631760477, Category.Sport, 385_000), //(Massacaro Race)
	MAVERICK(-1660661558, Category.Helicopter, 780_000),
	MESA(914654722, Category.SUV, 30_000),
	MESA2(-748008636, Category.SUV, Int.MAX_VALUE), //(Snowy Mesa)
	MESA3(-2064372143, Category.Offroad, 87_000), //(Merryweather Mesa)
	METROTRAIN(868868440, Category.Train, Int.MAX_VALUE),
	MICHELLI(1046206681, Category.SportsClassic, 1_225_000),
	MICROLIGHT(-1763555241, Category.Plane, 665_000),
	MILJET(165154707, Category.Plane, 1_750_000),
	MINIVAN(-310465116, Category.Van, 30_000),
	MINIVAN2(-1126264336, Category.Muscle, 360_000), //(Minivan Custom)
	MIXER(-784816453, Category.Truck, Int.MAX_VALUE),
	MIXER2(475220373, Category.Truck, Int.MAX_VALUE), //(Wheels On Back)
	MOGUL(-749299473, Category.Plane, 3_125_500),
	MOLOTOK(1565978651, Category.Plane, 4_788_000),
	MONROE(-433375717, Category.SportsClassic, 490_000),
	MONSTER(-845961253, Category.Offroad, 742_014), //(The Liberator)
	MOONBEAM(525509695, Category.Van, 32_500),
	MOONBEAM2(1896491931, Category.Van, 402_500), //(Moonbeam Custom)
	MOWER(1783355638, Category.Service, Int.MAX_VALUE),
	MULE(904750859, Category.Truck, 27_000),
	MULE2(-1050465301, Category.Truck, Int.MAX_VALUE), //(Drop Down Trunk)
	MULE3(-2052737935, Category.Truck, 43_225), //(No Livery)
	NEMESIS(-634879114, Category.Motorcycle, 12_000),
	NEON(-1848994066, Category.Sport, 1_500_000),
	NERO(1034187331, Category.Super, 1_440_000),
	NERO2(1093792632, Category.Super, 2_045_000), //(Nero Custom)
	NIGHTBLADE(-1606187161, Category.Motorcycle, 100_000),
	NIGHTSHADE(-1943285540, Category.Muscle, 585_000),
	NIGHTSHARK(433954513, Category.Offroad, 1_245_000),
	NIMBUS(-1295027632, Category.Plane, 1_900_000),
	NINEF(1032823388, Category.Sport, 120_000),
	NINEF2(-1461482751, Category.Sport, 130_000), //(Ninef Convertible)
	NOKOTA(1036591958, Category.Plane, 2_653_350),
	OMNIS(-777172681, Category.Sport, 701_000),
	OPPRESSOR(884483972, Category.Motorcycle, 2_650_000),
	ORACLE(1348744438, Category.Sedan, 82_000), //(GTA IV Oracle)
	ORACLE2(-511601230, Category.Sedan, 80_000), //(Oracle XS)
	OSIRIS(1987142870, Category.Super, 1_950_000),
	PACKER(569305213, Category.Truck, Int.MAX_VALUE),
	PANTO(-431692672, Category.Compact, 85_000),
	PARADISE(1488164764, Category.Van, 50_000),
	PARIAH(867799010, Category.Sport, 1_420_000),
	PATRIOT(-808457413, Category.SUV, 50_000),
	PBUS(-2007026063, Category.Emergency, 731_500), //(Prison Bus)
	PCJ(-909201658, Category.Motorcycle, 9_000),
	PENETRATOR(-1758137366, Category.Super, 880_000),
	PENUMBRA(-377465520, Category.Sport, 24_000),
	PEYOTE(1830407356, Category.SportsClassic, 38_000),
	PFISTER811(-1829802492, Category.Super, 1_135_000),
	PHANTOM(-2137348917, Category.Truck, Int.MAX_VALUE),
	PHANTOM2(-1649536104, Category.Truck, 2_553_600), //(Phantom Wedge)
	PHANTOM3(177270108, Category.Commercial, 1_225_000),
	PHOENIX(-2095439403, Category.Muscle, 20_000),
	PICADOR(1507916787, Category.Muscle, 9_000),
	PIGALLE(1078682497, Category.SportsClassic, 400_000),
	POLICE(2046537925, Category.Emergency, Int.MAX_VALUE), //(Police Stanier)
	POLICE2(-1627000575, Category.Emergency, Int.MAX_VALUE), //(Police Buffalo)
	POLICE3(1912215274, Category.Emergency, Int.MAX_VALUE), //(Police Interceptor)
	POLICE4(-1973172295, Category.Emergency, Int.MAX_VALUE), //(Undercover Police Stanier)
	POLICEB(-34623805, Category.Emergency, Int.MAX_VALUE), //(Police Bike)
	POLICEOLD1(-1536924937, Category.Emergency, Int.MAX_VALUE), //(Snowy Police Rancher)
	POLICEOLD2(-1779120616, Category.Emergency, Int.MAX_VALUE), //(Snowy Police Esperanto)
	POLICET(456714581, Category.Emergency, Int.MAX_VALUE), //(Police Transport Van)
	POLMAV(353883353, Category.Helicopter, Int.MAX_VALUE),
	PONY(-119658072, Category.Van, Int.MAX_VALUE), //(Sunset Bleach...)
	PONY2(943752001, Category.Van, Int.MAX_VALUE), //(Weed Van)
	POUNDER(2112052861, Category.Truck, Int.MAX_VALUE),
	PRAIRIE(-1450650718, Category.Compact, 25_000),
	PRANGER(741586030, Category.Emergency, 35_000),
	PREDATOR(-488123221, Category.Boat, Int.MAX_VALUE),
	PREMIER(-1883869285, Category.Sedan, 10_000),
	PRIMO(-1150599089, Category.Sedan, 9_000),
	PRIMO2(-2040426790, Category.Sedan, 409_000), //(Primo Custom)
	PROPTRAILER(356391690, Category.Trailer, Int.MAX_VALUE), //(Mobile Home Trailer)
	PROTOTIPO(2123327359, Category.Super, 2_700_000), //(X80 Proto)
	PYRO(-1386191424, Category.Plane, 4_455_500),
	RADI(-1651067813, Category.SUV, 32_000),
	RAIDEN(-1529242755, Category.Sport, 1_375_000),
	RAKETRAILER(390902130, Category.Trailer, Int.MAX_VALUE), //(Farm Cultivator)
	RALLYTRUCK(-2103821244, Category.Truck, 1_385_000), //(Dune)
	RANCHERXL(1645267888, Category.Offroad, 9_000),
	RANCHERXL2(1933662059, Category.Offroad, 9_000), //(Snowy Rancher)
	RAPIDGT(-1934452204, Category.Sport, 132_000),
	RAPIDGT2(1737773231, Category.Sport, 140_000), //(Rapid GT Convertible)
	RAPIDGT3(2049897956, Category.SportsClassic, 885_000),
	RAPTOR(-674927303, Category.Sport, 648_000),
	RATBIKE(1873600305, Category.Motorcycle, 48_000),
	RATLOADER(-667151410, Category.Pickup, 6_000),
	RATLOADER2(-589178377, Category.Pickup, 37_500), //(Rat Truck)
	REAPER(234062309, Category.Super, 1_595_000),
	REBEL(-1207771834, Category.Offroad, 3_000), //(Rusty Rebel)
	REBEL2(-2045594037, Category.Offroad, 22_000), //(Clean Rebel)
	REGINA(-14495224, Category.Sedan, 8_000),
	RENTALBUS(-1098802077, Category.Service, 30_000),
	RETINUE(1841130506, Category.SportsClassic, 615_000),
	REVOLTER(-410205223, Category.Sport, 1_610_000),
	RHAPSODY(841808271, Category.Compact, 140_000),
	RHINO(782665360, Category.Emergency, 1_500_000),
	RIATA(-1532697517, Category.Offroad, 380_000),
	RIOT(-1205689942, Category.Emergency, Int.MAX_VALUE),
	RIOT2(-1693015116, Category.Emergency, 3_125_500),
	RIPLEY(-845979911, Category.Service, Int.MAX_VALUE),
	ROCOTO(2136773105, Category.SUV, 85_000),
	ROGUE(-975345305, Category.Plane, 1_596_000),
	ROMERO(627094268, Category.Sedan, 45_000),
	RUBBLE(-1705304628, Category.Truck, Int.MAX_VALUE),
	RUFFIAN(-893578776, Category.Motorcycle, 10_000),
	RUINER(-227741703, Category.Muscle, 10_000),
	RUINER2(941494461, Category.Sport, 5_745_600), //(Ruiner 2000)
	RUINER3(777714999, Category.Sport, Int.MAX_VALUE), //(Destroyed)
	RUMPO(1162065741, Category.Van, 13_000), //(Weazel News Rumpo)
	RUMPO2(-1776615689, Category.Van, Int.MAX_VALUE), //(Deludamol Rumpo)
	RUMPO3(1475773103, Category.Van, 130_000), //(Rumpo Custom)
	RUSTON(719660200, Category.Sport, 430_000),
	SABREGT(-1685021548, Category.Muscle, 15_000),
	SABREGT2(223258115, Category.Muscle, 480_500), //(Sabre Turbo Custom)
	SADLER(-599568815, Category.Pickup, 35_000),
	SADLER2(734217681, Category.Pickup, Int.MAX_VALUE), //(Snowy Sadler)
	SANCHEZ(788045382, Category.Motorcycle, 7_000), //(Sanchez Livery)
	SANCHEZ2(-1453280962, Category.Motorcycle, 8_000), //(Sanchez Paint)
	SANCTUS(1491277511, Category.Motorcycle, 1_995_000),
	SANDKING(-1189015600, Category.Offroad, 45_000), //(Sandking 4-Seater)
	SANDKING2(989381445, Category.Offroad, 38_000), //(Sandking 2-Seater)
	SAVAGE(-82626025, Category.Helicopter, 2_593_500),
	SAVESTRA(903794909, Category.SportsClassic, 990_000),
	SC1(1352136073, Category.Super, 1_603_000),
	SCHAFTER2(-1255452397, Category.Sedan, 65_000),
	SCHAFTER3(-1485523546, Category.Sedan, 116_000), //(Schafter V12)
	SCHAFTER4(1489967196, Category.Sedan, 208_000), //(Schafter LWB)
	SCHAFTER5(-888242983, Category.Sedan, 325_000), //(Schafter V12 (Armored)
	SCHAFTER6(1922255844, Category.Sedan, 438_000), //(Schafter LWB (Armored)
	SCHWARZER(-746882698, Category.Sport, 80_000),
	SCORCHER(-186537451, Category.Bicycle, 2_000),
	SCRAP(-1700801569, Category.Truck, Int.MAX_VALUE),
	SEABREEZE(-392675425, Category.Plane, 1_130_500),
	SEASHARK(-1030275036, Category.Boat, 16_899), //(Speedophile Seashark)
	SEASHARK2(-616331036, Category.Boat, Int.MAX_VALUE), //(Lifeguard Seashark)
	SEASHARK3(-311022263, Category.Boat, Int.MAX_VALUE), //(Yacht Seashark)
	SEASPARROW(-726768679, Category.Helicopter, 1_815_000),
	SEMINOLE(1221512915, Category.SUV, 30_000),
	SENTINEL(1349725314, Category.Coupe, 60_000), //(Sentinel XS)
	SENTINEL2(873639469, Category.Coupe, 95_000), //(Sentinel Convertible)
	SENTINEL3(1104234922, Category.Sport, 650_000),
	SERRANO(1337041428, Category.SUV, 60_000),
	SEVEN70(-1757836725, Category.Sport, 695_000),
	SHAMAL(-1214505995, Category.Plane, 1_150_000),
	SHEAVA(819197656, Category.Super, 1_995_000), //(ETR1)
	SHERIFF(-1683328900, Category.Emergency, Int.MAX_VALUE), //(Sheriff Stanier)
	SHERIFF2(1922257928, Category.Emergency, Int.MAX_VALUE), //(Sheriff Granger)
	SHOTARO(-405626514, Category.Motorcycle, 2_225_000),
	SKYLIFT(1044954915, Category.Helicopter, Int.MAX_VALUE),
	SLAMVAN(729783779, Category.Muscle, 49_500),
	SLAMVAN2(833469436, Category.Muscle, Int.MAX_VALUE), //(Lost Slamvan)
	SLAMVAN3(1119641113, Category.Muscle, 464_500), //(Slamvan Custom)
	SOVEREIGN(743478836, Category.Motorcycle, 120_000),
	SPECTER(1886268224, Category.Sport, 599_000),
	SPECTER2(1074745671, Category.Sport, 851_000), //(Specter Custom)
	SPEEDER(231083307, Category.Boat, 325_000),
	SPEEDER2(437538602, Category.Boat, Int.MAX_VALUE), //(Yacht Speeder)
	SPEEDO(-810318068, Category.Van, 15_000),
	SPEEDO2(728614474, Category.Van, 15_000), //(Clown Van)
	SQUALO(400514754, Category.Boat, 196_621),
	STALION(1923400478, Category.Muscle, 71_000),
	STALION2(-401643538, Category.Muscle, 277_000), //(Burger Shot Stallion)
	STANIER(-1477580979, Category.Sedan, 10_000),
	STARLING(-1700874274, Category.Plane, 3_657_500),
	STINGER(1545842587, Category.SportsClassic, 850_000),
	STINGERGT(-2098947590, Category.SportsClassic, 875_000),
	STOCKADE(1747439474, Category.Truck, Int.MAX_VALUE),
	STOCKADE3(-214455498, Category.Truck, Int.MAX_VALUE), //(Snowy Stockade)
	STRATUM(1723137093, Category.Sedan, 10_000),
	STREITER(1741861769, Category.Sport, 500_000),
	STRETCH(-1961627517, Category.Sedan, 30_000),
	STROMBERG(886810209, Category.SportsClassic, 3_185_350),
	STUNT(-2122757008, Category.Plane, 250_000),
	SUBMERSIBLE(771711535, Category.Boat, Int.MAX_VALUE),
	SUBMERSIBLE2(-1066334226, Category.Boat, 1_325_000), //(Kraken)
	SULTAN(970598228, Category.Sport, 12_000),
	SULTANRS(-295689028, Category.Super, 807_000),
	SUNTRAP(-282946103, Category.Boat, 25_160),
	SUPERD(1123216662, Category.Sedan, 250_000),
	SUPERVOLITO(710198397, Category.Helicopter, 2_113_000),
	SUPERVOLITO2(-1671539132, Category.Helicopter, 3_330_000), //(SuperVolito Carbon)
	SURANO(384071873, Category.Sport, 110_000),
	SURFER(699456151, Category.Van, 11_000),
	SURFER2(-1311240698, Category.Van, 5_000), //(Rusty Surfer)
	SURGE(-1894894188, Category.Sedan, 38_000),
	SWIFT(-339587598, Category.Helicopter, 1_600_000),
	SWIFT2(1075432268, Category.Helicopter, 5_150_000), //(Gold)
	T20(1663218586, Category.Super, 2_200_000),
	TACO(1951180813, Category.Van, Int.MAX_VALUE),
	TAILGATER(-1008861746, Category.Sedan, 55_000),
	TAIPAN(-1134706562, Category.Super, 1_980_000),
	TAMPA(972671128, Category.Muscle, 375_000),
	TAMPA2(-1071380347, Category.Muscle, 995_000), //(Drift Tampa)
	TAMPA3(-1210451983, Category.Muscle, 1_585_000),
	TANKER(-730904777, Category.Trailer, Int.MAX_VALUE),
	TANKER2(1956216962, Category.Trailer, Int.MAX_VALUE), //(No Livery)
	TANKERCAR(586013744, Category.Train, Int.MAX_VALUE), //(Train Fuel Tank Car)
	TAXI(-956048545, Category.Sedan, 13_000),
	TECHNICAL(-2096818938, Category.Offroad, 1_263_500),
	TECHNICAL2(1180875963, Category.Offroad, 1_489_600), //(Technical Aqua)
	TECHNICAL3(1356124575, Category.Offroad, 1_406_000),
	TEMPESTA(272929391, Category.Super, 1_329_000),
	TEZERACT(1031562256, Category.Super, 2_825_000),
	THRUST(1836027715, Category.Motorcycle, 75_000),
	THRUSTER(1489874736, Category.Military, 3_657_500),
	TIPTRUCK(48339065, Category.Truck, Int.MAX_VALUE), //(6-Wheeler)
	TIPTRUCK2(-947761570, Category.Truck, Int.MAX_VALUE), //(10-Wheeler)
	TITAN(1981688531, Category.Plane, 5_000_000),
	TORERO(1504306544, Category.SportsClassic, 998_000),
	TORNADO(464687292, Category.SportsClassic, 30_000),
	TORNADO2(1531094468, Category.SportsClassic, 30_000), //(Convertible)
	TORNADO3(1762279763, Category.SportsClassic, 30_000), //(Rusty)
	TORNADO4(-2033222435, Category.SportsClassic, 30_000), //(Mariachi)
	TORNADO5(-1797613329, Category.Muscle, 405_000), //(Custom)
	TORNADO6(-1558399629, Category.Muscle, 378_000), //(Rat Rod)
	TORO(1070967343, Category.Boat, 1_750_000),
	TORO2(908897389, Category.Boat, Int.MAX_VALUE), //(Yacht Toro)
	TOURBUS(1941029835, Category.Service, Int.MAX_VALUE),
	TOWTRUCK(-1323100960, Category.Truck, Int.MAX_VALUE), //(Large Towtruck)
	TOWTRUCK2(-442313018, Category.Truck, 32_000), //(Small Towtruck)
	TR2(2078290630, Category.Trailer, Int.MAX_VALUE), //(Car Carrier Trailer)
	TR3(1784254509, Category.Trailer, Int.MAX_VALUE), //(Marquis Trailer)
	TR4(2091594960, Category.Trailer, Int.MAX_VALUE), //(Super Car Carrier Trailer)
	TRACTOR(1641462412, Category.Service, Int.MAX_VALUE), //(Rusty Tractor)
	TRACTOR2(-2076478498, Category.Service, Int.MAX_VALUE), //(Farm Tractor)
	TRACTOR3(1445631933, Category.Service, Int.MAX_VALUE), //(Snowy Tractor)
	TRAILERLARGE(1502869817, Category.Utility, Int.MAX_VALUE),
	TRAILERLOGS(2016027501, Category.Trailer, Int.MAX_VALUE), //(Log Trailer)
	TRAILERS(-877478386, Category.Trailer, Int.MAX_VALUE), //(Metal/Tarp Covered Trailer)
	TRAILERS2(-1579533167, Category.Trailer, Int.MAX_VALUE), //(Up & Atom...)
	TRAILERS3(-2058878099, Category.Trailer, Int.MAX_VALUE), //(Biggoods Trailer)
	TRAILERS4(-1100548694, Category.Utility, Int.MAX_VALUE),
	TRAILERSMALL(712162987, Category.Trailer, Int.MAX_VALUE), //(Small Construction Trailer)
	TRAILERSMALL2(-1881846085, Category.Military, 1_400_000),
	TRASH(1917016601, Category.Service, Int.MAX_VALUE),
	TRASH2(-1255698084, Category.Service, Int.MAX_VALUE), //(Rusty Back)
	TRFLAT(-1352468814, Category.Trailer, Int.MAX_VALUE), //(Flatbed Trailer)
	TRIBIKE(1127861609, Category.Bicycle, 10_000), //(Green Whippet Race Bike)
	TRIBIKE2(-1233807380, Category.Bicycle, 10_000), //(Red Endurex Race Bike)
	TRIBIKE3(-400295096, Category.Bicycle, 10_000), //(Blue Tri-Cycles Race Bike)
	TROPHYTRUCK(101905590, Category.Offroad, 550_000),
	TROPHYTRUCK2(-663299102, Category.Offroad, 695_000), //(Desert Raid)
	TROPIC(290013743, Category.Boat, 22_000),
	TROPIC2(1448677353, Category.Boat, Int.MAX_VALUE), //(Yacht Tropic)
	TROPOS(1887331236, Category.Sport, 816_000), //(Tropos Rallye)
	TUG(-2100640717, Category.Boat, 1_250_000),
	TULA(1043222410, Category.Plane, 5_173_700),
	TURISMO2(-982130927, Category.Super, 705_000),
	TURISMOR(408192225, Category.Super, 500_000), //(Turismo Classic)
	TVTRAILER(-1770643266, Category.Trailer, Int.MAX_VALUE), //(Fame or Shame Trailer)
	TYRANT(-376434238, Category.Super, 2_515_000),
	TYRUS(2067820283, Category.Super, 2_550_000),
	UTILLITRUCK(516990260, Category.Service, Int.MAX_VALUE), //(Bldg/Renov Basket Truck)
	UTILLITRUCK2(887537515, Category.Service, Int.MAX_VALUE), //(Landscape...)
	UTILLITRUCK3(2132890591, Category.Service, Int.MAX_VALUE), //(Landscape Utility Pick-up)
	VACCA(338562499, Category.Super, 240_000),
	VADER(-140902153, Category.Motorcycle, 9_000),
	VAGNER(1939284556, Category.Super, 1_535_000),
	VALKYRIE(-1600252419, Category.Helicopter, 3_790_500),
	VALKYRIE2(1543134283, Category.Helicopter, Int.MAX_VALUE), //(Valkyrie MOD.0)
	VELUM(-1673356438, Category.Plane, 450_000),
	VELUM2(1077420264, Category.Plane, 1_323_350), //(5-Seater)
	VERLIERER2(1102544804, Category.Sport, 695_000),
	VESTRA(1341619767, Category.Plane, 950_000),
	VIGERO(-825837129, Category.Muscle, 21_000),
	VIGILANTE(-1242608589, Category.Military, 3_750_000),
	VINDICATOR(-1353081087, Category.Motorcycle, 630_000),
	VIRGO(-498054846, Category.Muscle, 195_000),
	VIRGO2(-899509638, Category.Muscle, 405_000), //(Virgo Classic Custom)
	VIRGO3(16646064, Category.Muscle, 165_000), //(Virgo Classic)
	VISERIS(-391595372, Category.SportsClassic, 875_000),
	VISIONE(-998177792, Category.Super, 2_250_000),
	VOLATOL(447548909, Category.Plane, 3_724_000),
	VOLATUS(-1845487887, Category.Helicopter, 2_295_000),
	VOLTIC(-1622444098, Category.Super, 150_000),
	VOLTIC2(989294410, Category.Super, 3_830_400), //(Rocket Voltic)
	VOODOO(2006667053, Category.Muscle, 425_500), //(Voodoo Custom)
	VOODOO2(523724515, Category.Muscle, 5_500), //(Rusty Voodoo)
	VORTEX(-609625092, Category.Motorcycle, 356_000),
	WARRENER(1373123368, Category.Sedan, 125_000),
	WASHINGTON(1777363799, Category.Sedan, 15_000),
	WASTELANDER(-1912017790, Category.Super, 658_350),
	WINDSOR(1581459400, Category.Coupe, 845_000),
	WINDSOR2(-1930048799, Category.Coupe, 900_000), //(Windsor Drop)
	WOLFSBANE(-618617997, Category.Motorcycle, 95_000),
	XA21(917809321, Category.Super, 2_375_000),
	XLS(1203490606, Category.SUV, 253_000),
	XLS2(-432008408, Category.SUV, 522_000), //(XLS (Armored)
	YOSEMITE(1871995513, Category.Muscle, 485_000),
	YOUGA(65402552, Category.Van, 16_000),
	YOUGA2(1026149675, Category.Van, 195_000), //(Youga Classic)
	Z190(838982985, Category.SportsClassic, 900_000),
	ZENTORNO(-1403128555, Category.Super, 725_000),
	ZION(-1122289213, Category.Coupe, 60_000), //(Zion XS)
	ZION2(-1193103848, Category.Coupe, 65_000), //(Zion Convertible)
	ZOMBIEA(-1009268949, Category.Motorcycle, 99_000), //(Zombie Bobber)
	ZOMBIEB(-570033273, Category.Motorcycle, 122_000), //(Zombie Chopper)
	ZTYPE(758895617, Category.SportsClassic, 950_000)
	;

	enum class Category {
		Super, Coupe, SportsClassic, Motorcycle, Sedan, Van, Muscle, SUV, Plane, Sport, Military, Helicopter, Utility, Trailer, Service, Offroad, Bicycle, Truck, Boat, Train, Commercial, Emergency, Compact, Pickup, AFTER
	}
}

