package online.fivem.common.gtav

import online.fivem.common.entities.Coordinates

//https://wiki.gtanet.work/index.php?title=Online_Interiors_and_locations

@Suppress("SpellCheckingInspection")
enum class NativeIPl(vararg val code: String, val coordinates: Coordinates? = null) {
	// Simeon:
	SHR_INT("shr_int", coordinates = Coordinates(-47.16170f, -1115.3327f, 26.5f)),

	// Trevor:
	// Trash or Tidy. Only choose one.
	TREVORS_TRAILER_TRASH("TrevorsTrailerTrash", coordinates = Coordinates(1985.48132f, 3828.76757f, 32.5f)),
	TREVORS_TRAILER_TIDY("trevorstrailertidy", coordinates = TREVORS_TRAILER_TRASH.coordinates),

	// Heist Union Depository:
	FINBANK("FINBANK", coordinates = Coordinates(2.69689322f, -667.0166f, 16.1306286f)),

	// Cluckin Bell:
	CS1_02_CF_ONMISSION1("CS1_02_cf_onmission1", coordinates = Coordinates(-146.3837f, 6161.5f, 30.2062f)),
	CS1_02_CF_ONMISSION2("CS1_02_cf_onmission2", coordinates = CS1_02_CF_ONMISSION1.coordinates),
	CS1_02_CF_ONMISSION3("CS1_02_cf_onmission3", coordinates = CS1_02_CF_ONMISSION1.coordinates),
	CS1_02_CF_ONMISSION4("CS1_02_cf_onmission4", coordinates = CS1_02_CF_ONMISSION1.coordinates),

	// Grapeseed's farm:
	GRAPESEEDS_FARM(
		"farm",
		"farmint",
		"farm_lod",
		"farm_props",
		"des_farmhouse",
		coordinates = Coordinates(2447.9f, 4973.4f, 47.7f)
	),

	// Billboard: iFruit
	IFRUIT_BILLBOARD("FruitBB"),
	SC1_01_NEWBILL("sc1_01_newbill"),
	HW1_02_NEWBILL("hw1_02_newbill"),
	HW1_EMISSIVE_NEWBILL("hw1_emissive_newbill"),
	SC1_14_NEWBILL("sc1_14_newbill"),
	DT1_17_NEWBILL("dt1_17_newbill"),

	// Lester's factory:
	LESTERS_FACTORY("id2_14_during_door", "id2_14_during1", coordinates = Coordinates(716.84f, -962.05f, 31.59f)),

	// Life Invader lobby:
	LIFE_INVADER_LOBBY("facelobby", coordinates = Coordinates(-1047.9f, -233.0f, 39.0f)),

	// Tunnels
	V_TUNNEL_HOLE("v_tunnel_hole"),

	// Carwash:
	CARWASH_WITH_SPINNERS("Carwash_with_spinners", coordinates = Coordinates(55.7f, -1391.3f, 30.5f)),

	// Stadium "Fame or Shame":
	STADIUM_FLAME_OR_SHAME(
		"sp1_10_real_interior",
		"sp1_10_real_interior_lod",
		coordinates = Coordinates(-248.49159240722656f, -2010.509033203125f, 34.57429885864258f)
	),

	// House in Banham Canyon:
	HOUSE_IN_BANHAM_CANYON("ch1_02_open", coordinates = Coordinates(-3086.428f, 339.2523f, 6.3717f)),

	// Garage in La Mesa (autoshop):
	GARALE_IN_LA_MESA_AUTOSHOP("bkr_bi_id1_23_door", coordinates = Coordinates(970.27453f, -1826.56982f, 31.11477f)),

	// Hill Valley church - Grave:
	HILL_VALLEY_CHURCH_GRAVE(
		"lr_cs6_08_grave_closed",
		coordinates = Coordinates(-282.46380000f, 2835.84500000f, 55.91446000f)
	),

	// Lost's trailer park:
	LOSTS_TRAILER_PARK("methtrailer_grp1", coordinates = Coordinates(49.49379000f, 3744.47200000f, 46.38629000f)),

	// Lost safehouse:
	LOSTS_SAFEHOUSE("bkr_bi_hw1_13_int", coordinates = Coordinates(984.1552f, -95.3662f, 74.50f)),

	// Raton Canyon river:
	RATON_CANYON_RIVER("CanyonRvrShallow", coordinates = Coordinates(-1652.83f, 4445.28f, 2.52f)),

	// Zancudo Gates (GTAO like):
	ZANCUDO_GATES_GTAO_LIKE("CS3_07_MPGates", coordinates = Coordinates(-1600.30100000f, 2806.73100000f, 18.79683000f)),

	// Pillbox hospital:
	PILLBOX_HOSPITAL("rc12b_default"),

	// Josh's house:
	JOSHS_HOUSE(
		"bh1_47_joshhse_unburnt",
		"bh1_47_joshhse_unburnt_lod",
		coordinates = Coordinates(-1117.1632080078f, 303.090698f, 66.52217f)
	),

	// Zancudo River (need streamed content):
	ZANCUDO_RIVER(
		"cs3_05_water_grp1",
		"cs3_05_water_grp1_lod",
		"cs3_05_water_grp2",
		"cs3_05_water_grp2_lod",
		coordinates = Coordinates(86.815f, 3191.649f, 30.463f)
	),

	// Optional
	// Graffitis
	ch3_rd2_bishopschickengraffiti(
		"ch3_rd2_bishopschickengraffiti",
		coordinates = Coordinates(1861.28f, 2402.11f, 58.53f)
	),
	cs5_04_mazebillboardgraffiti("cs5_04_mazebillboardgraffiti", coordinates = Coordinates(2697.32f, 3162.18f, 58.1f)),
	cs5_roads_ronoilgraffiti("cs5_roads_ronoilgraffiti", coordinates = Coordinates(2119.12f, 3058.21f, 53.25f)),

	// Bunkers - Interior:
	BUNKERS_INTERIOR(
		"gr_entrance_placement",
		"gr_grdlc_interior_placement",
		"gr_grdlc_interior_placement_interior_0_grdlc_int_01_milo_",
		"gr_grdlc_interior_placement_interior_1_grdlc_int_02_milo_",
		coordinates = Coordinates(892.6384f, -3245.8664f, -98.2645f)
	),

	// Bahama Mamas:
	BAHAMA_MAMAS("hei_sm_16_interior_v_bahama_milo_", coordinates = Coordinates(-1388.0013f, -618.41967f, 30.819599f)),

	UFO_ZANCUDO("ufo", coordinates = Coordinates(-2051.99463f, 3237.05835f, 1456.97021f)),
	UFO_EYE_HIPPIE_BASE("ufo_eye", coordinates = Coordinates(2490.47729f, 3774.84351f, 2414.035f)),
	UFO_LOD_CHILLIAD("ufo_lod", coordinates = Coordinates(501.52880000f, 5593.86500000f, 796.23250000f)),

	// Biker
	BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_0_BIKER_DLC_INT_01_MILO("bkr_biker_interior_placement_interior_0_biker_dlc_int_01_milo"),
	BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_1_BIKER_DLC_INT_02_MILO("bkr_biker_interior_placement_interior_1_biker_dlc_int_02_milo"),

	BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE01_MILO("bkr_biker_interior_placement_interior_2_biker_dlc_int_ware01_milo"),
	BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE02_MILO("bkr_biker_interior_placement_interior_2_biker_dlc_int_ware02_milo"),
	BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE03_MILO("bkr_biker_interior_placement_interior_2_biker_dlc_int_ware03_milo"),
	BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE04_MILO("bkr_biker_interior_placement_interior_2_biker_dlc_int_ware04_milo"),
	BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE05_MILO("bkr_biker_interior_placement_interior_2_biker_dlc_int_ware05_milo"),

	EX_EXEC_WAREHOUSE_PLACEMENT_INTERIOR_0_INT_WAREHOUSE_M_DLC_MILO("ex_exec_warehouse_placement_interior_0_int_warehouse_m_dlc_milo "),
	EX_EXEC_WAREHOUSE_PLACEMENT_INTERIOR_1_INT_WAREHOUSE_S_DLC_MILO("ex_exec_warehouse_placement_interior_1_int_warehouse_s_dlc_milo "),
	EX_EXEC_WAREHOUSE_PLACEMENT_INTERIOR_2_INT_WAREHOUSE_L_DLC_MILO("ex_exec_warehouse_placement_interior_2_int_warehouse_l_dlc_milo "),

	// IMPORT/EXPORT
	imp_dt1_02_modgarage("imp_dt1_02_modgarage"),
	imp_dt1_02_cargarage_a("imp_dt1_02_cargarage_a"),
	imp_dt1_02_cargarage_b("imp_dt1_02_cargarage_b"),
	imp_dt1_02_cargarage_c("imp_dt1_02_cargarage_c"),

	imp_dt1_11_modgarage("imp_dt1_11_modgarage"),
	imp_dt1_11_cargarage_a("imp_dt1_11_cargarage_a"),
	imp_dt1_11_cargarage_b("imp_dt1_11_cargarage_b"),
	imp_dt1_11_cargarage_c("imp_dt1_11_cargarage_c"),

	imp_sm_13_modgarage("imp_sm_13_modgarage"),
	imp_sm_13_cargarage_a("imp_sm_13_cargarage_a"),
	imp_sm_13_cargarage_b("imp_sm_13_cargarage_b"),
	imp_sm_13_cargarage_c("imp_sm_13_cargarage_c"),

	imp_sm_15_modgarage("imp_sm_15_modgarage"),
	imp_sm_15_cargarage_a("imp_sm_15_cargarage_a"),
	imp_sm_15_cargarage_b("imp_sm_15_cargarage_b"),
	imp_sm_15_cargarage_c("imp_sm_15_cargarage_c"),

	imp_impexp_interior_placement("imp_impexp_interior_placement"),
	imp_impexp_interior_placement_interior_0_impexp_int_01_milo_("imp_impexp_interior_placement_interior_0_impexp_int_01_milo_"),
	imp_impexp_interior_placement_interior_3_impexp_int_02_milo_("imp_impexp_interior_placement_interior_3_impexp_int_02_milo_"),
	imp_impexp_interior_placement_interior_1_impexp_intwaremed_milo_("imp_impexp_interior_placement_interior_1_impexp_intwaremed_milo_"),
	imp_impexp_interior_placement_interior_2_imptexp_mod_int_01_milo_("imp_impexp_interior_placement_interior_2_imptexp_mod_int_01_milo_"),

	//\t(.*)\t([A-z0-9_]+)\tnew Vector3\((-?[0-9]+\.[0-9]+), (-?[0-9]+\.[0-9]+), (-?[0-9]+\.[0-9]+)\);(\t+[A-z0-9]+)?
//$1("$2", Coordinates($3f, $4f, $5f)),//$6
//	Online Bunkers
	ZANCUDO_BUNKER("gr_case10_bunkerclosed", coordinates = Coordinates(-3058.714f, 3329.19f, 12.5844f)),//
	ROUTE68_BUNKER("gr_case9_bunkerclosed", coordinates = Coordinates(24.43542f, 2959.705f, 58.35517f)),//
	OILFIELD_SBUNKER("gr_case3_bunkerclosed", coordinates = Coordinates(481.0465f, 2995.135f, 43.96672f)),//
	DESERT_BUNKER("gr_case0_bunkerclosed", coordinates = Coordinates(848.6175f, 2996.567f, 45.81612f)),//
	SMOKETREE_BUNKER("gr_case1_bunkerclosed", coordinates = Coordinates(2126.785f, 3335.04f, 48.21422f)),//
	SCRAPYARD_BUNKER("gr_case2_bunkerclosed", coordinates = Coordinates(2493.654f, 3140.399f, 51.28789f)),//
	GRAPESEED_BUNKER("gr_case5_bunkerclosed", coordinates = Coordinates(1823.961f, 4708.14f, 42.4991f)),//
	PALLETO_BUNKER("gr_case7_bunkerclosed", coordinates = Coordinates(-783.0755f, 5934.686f, 24.31475f)),//
	ROUTE1_BUNKER("gr_case11_bunkerclosed", coordinates = Coordinates(-3180.466f, 1374.192f, 19.9597f)),//
	FARMHOUSE_BUNKER("gr_case6_bunkerclosed", coordinates = Coordinates(1570.372f, 2254.549f, 78.89397f)),//
	RATON_CANYON_BUNKER("gr_case4_bunkerclosed", coordinates = Coordinates(-391.3216f, 4363.728f, 58.65862f)),//

	//	Online Apartments
//	Name	IPL	Vector3
	MODERN_APARTMENT_1("apa_v_mp_h_01_a", coordinates = Coordinates(-786.8663f, 315.7642f, 217.6385f)),//
	MODERN_APARTMENT_2("apa_v_mp_h_01_c", coordinates = Coordinates(-786.9563f, 315.6229f, 187.9136f)),//
	MODERN_APARTMENT_3("apa_v_mp_h_01_b", coordinates = Coordinates(-774.0126f, 342.0428f, 196.6864f)),//
	MODY_APARTMENT_1("apa_v_mp_h_02_a", coordinates = Coordinates(-787.0749f, 315.8198f, 217.6386f)),//
	MODY_APARTMENT_2("apa_v_mp_h_02_c", coordinates = Coordinates(-786.8195f, 315.5634f, 187.9137f)),//
	MODY_APARTMENT_3("apa_v_mp_h_02_b", coordinates = Coordinates(-774.1382f, 342.0316f, 196.6864f)),//
	VIBRANT_APARTMENT_1("apa_v_mp_h_03_a", coordinates = Coordinates(-786.6245f, 315.6175f, 217.6385f)),//
	VIBRANT_APARTMENT_2("apa_v_mp_h_03_c", coordinates = Coordinates(-786.9584f, 315.7974f, 187.9135f)),//
	VIBRANT_APARTMENT_3("apa_v_mp_h_03_b", coordinates = Coordinates(-774.0223f, 342.1718f, 196.6863f)),//
	SHARP_APARTMENT_1("apa_v_mp_h_04_a", coordinates = Coordinates(-787.0902f, 315.7039f, 217.6384f)),//
	SHARP_APARTMENT_2("apa_v_mp_h_04_c", coordinates = Coordinates(-787.0155f, 315.7071f, 187.9135f)),//
	SHARP_APARTMENT_3("apa_v_mp_h_04_b", coordinates = Coordinates(-773.8976f, 342.1525f, 196.6863f)),//
	MONOCHROME_APARTMENT_1("apa_v_mp_h_05_a", coordinates = Coordinates(-786.9887f, 315.7393f, 217.6386f)),//
	MONOCHROME_APARTMENT_2("apa_v_mp_h_05_c", coordinates = Coordinates(-786.8809f, 315.6634f, 187.9136f)),//
	MONOCHROME_APARTMENT_3("apa_v_mp_h_05_b", coordinates = Coordinates(-774.0675f, 342.0773f, 196.6864f)),//
	SEDUCTIVE_APARTMENT_1("apa_v_mp_h_06_a", coordinates = Coordinates(-787.1423f, 315.6943f, 217.6384f)),//
	SEDUCTIVE_APARTMENT_2("apa_v_mp_h_06_c", coordinates = Coordinates(-787.0961f, 315.815f, 187.9135f)),//
	SEDUCTIVE_APARTMENT_3("apa_v_mp_h_06_b", coordinates = Coordinates(-773.9552f, 341.9892f, 196.6862f)),//
	REGAL_APARTMENT_1("apa_v_mp_h_07_a", coordinates = Coordinates(-787.029f, 315.7113f, 217.6385f)),//
	REGAL_APARTMENT_2("apa_v_mp_h_07_c", coordinates = Coordinates(-787.0574f, 315.6567f, 187.9135f)),//
	REGAL_APARTMENT_3("apa_v_mp_h_07_b", coordinates = Coordinates(-774.0109f, 342.0965f, 196.6863f)),//
	AQUA_APARTMENT_1("apa_v_mp_h_08_a", coordinates = Coordinates(-786.9469f, 315.5655f, 217.6383f)),//
	AQUA_APARTMENT_2("apa_v_mp_h_08_c", coordinates = Coordinates(-786.9756f, 315.723f, 187.9134f)),//
	AQUA_APARTMENT_3("apa_v_mp_h_08_b", coordinates = Coordinates(-774.0349f, 342.0296f, 196.6862f)),//

	//	Arcadius Business Centre
//	Name	IPL	Vector3	Categories
	EXECUTIVE_RICH("ex_dt1_02_office_02b", coordinates = Coordinates(-141.1987f, -620.913f, 168.8205f)),//	Office
	EXECUTIVE_COOL("ex_dt1_02_office_02c", coordinates = Coordinates(-141.5429f, -620.9524f, 168.8204f)),//	Office
	EXECUTIVE_CONTRAST("ex_dt1_02_office_02a", coordinates = Coordinates(-141.2896f, -620.9618f, 168.8204f)),//	Office
	OLD_SPICE_WARM("ex_dt1_02_office_01a", coordinates = Coordinates(-141.4966f, -620.8292f, 168.8204f)),//	Office
	OLD_SPICE_CLASSICAL("ex_dt1_02_office_01b", coordinates = Coordinates(-141.3997f, -620.9006f, 168.8204f)),//	Office
	OLD_SPICE_VINTAGE("ex_dt1_02_office_01c", coordinates = Coordinates(-141.5361f, -620.9186f, 168.8204f)),//	Office
	POWER_BROKER_ICE("ex_dt1_02_office_03a", coordinates = Coordinates(-141.392f, -621.0451f, 168.8204f)),//	Office
	POWER_BROKER_CONSERVATIVE(
		"ex_dt1_02_office_03b",
		coordinates = Coordinates(-141.1945f, -620.8729f, 168.8204f)
	),//	Office
	POWER_BROKER_POLISHED(
		"ex_dt1_02_office_03c",
		coordinates = Coordinates(-141.4924f, -621.0035f, 168.8205f)
	),//	Office

	//	Maze Bank Building
//	Name	IPL	Vector3	Categories
	EXECUTIVE_RICH_11("ex_dt1_11_office_02b", coordinates = Coordinates(-75.8466f, -826.9893f, 243.3859f)),//	Office
	EXECUTIVE_COOL_11("ex_dt1_11_office_02c", coordinates = Coordinates(-75.49945f, -827.05f, 243.386f)),//	Office
	EXECUTIVE_CONTRAST_11("ex_dt1_11_office_02a", coordinates = Coordinates(-75.49827f, -827.1889f, 243.386f)),//	Office
	OLD_SPICE_WARM_11("ex_dt1_11_office_01a", coordinates = Coordinates(-75.44054f, -827.1487f, 243.3859f)),//	Office
	OLD_SPICE_CLASSICAL_11(
		"ex_dt1_11_office_01b",
		coordinates = Coordinates(-75.63942f, -827.1022f, 243.3859f)
	),//	Office
	OLD_SPICE_VINTAGE_11("ex_dt1_11_office_01c", coordinates = Coordinates(-75.47446f, -827.2621f, 243.386f)),//	Office
	POWER_BROKER_ICE_11("ex_dt1_11_office_03a", coordinates = Coordinates(-75.56978f, -827.1152f, 243.3859f)),//	Office
	POWER_BROKER_CONSERVATIVE_11(
		"ex_dt1_11_office_03b",
		coordinates = Coordinates(-75.51953f, -827.0786f, 243.3859f)
	),//	Office
	POWER_BROKER_POLISHED_11(
		"ex_dt1_11_office_03c",
		coordinates = Coordinates(-75.41915f, -827.1118f, 243.3858f)
	),//	Office

	//	Lom Bank
//	Name	IPL	Vector3	Categories
	EXECUTIVE_RICH_13("ex_sm_13_office_02b", coordinates = Coordinates(-1579.756f, -565.0661f, 108.523f)),//	Office
	EXECUTIVE_COOL_13("ex_sm_13_office_02c", coordinates = Coordinates(-1579.678f, -565.0034f, 108.5229f)),//	Office
	EXECUTIVE_CONTRAST_13("ex_sm_13_office_02a", coordinates = Coordinates(-1579.583f, -565.0399f, 108.5229f)),//	Office
	OLD_SPICE_WARM_13("ex_sm_13_office_01a", coordinates = Coordinates(-1579.702f, -565.0366f, 108.5229f)),//	Office
	OLD_SPICE_CLASSICAL_13(
		"ex_sm_13_office_01b",
		coordinates = Coordinates(-1579.643f, -564.9685f, 108.5229f)
	),//	Office
	OLD_SPICE_VINTAGE_13("ex_sm_13_office_01c", coordinates = Coordinates(-1579.681f, -565.0003f, 108.523f)),//	Office
	POWER_BROKER_ICE_13("ex_sm_13_office_03a", coordinates = Coordinates(-1579.677f, -565.0689f, 108.5229f)),//	Office
	POWER_BROKER_CONSERVATIVE_13(
		"ex_sm_13_office_03b",
		coordinates = Coordinates(-1579.708f, -564.9634f, 108.5229f)
	),//	Office
	POWER_BROKER_POLISHED_13(
		"ex_sm_13_office_03c",
		coordinates = Coordinates(-1579.693f, -564.8981f, 108.5229f)
	),//	Office

	//	Maze Bank West
//	Name	IPL	Vector3	Categories
	EXECUTIVE_RICH_15("ex_sm_15_office_02b", coordinates = Coordinates(-1392.667f, -480.4736f, 72.04217f)),//	Office
	EXECUTIVE_COOL_15("ex_sm_15_office_02c", coordinates = Coordinates(-1392.542f, -480.4011f, 72.04211f)),//	Office
	EXECUTIVE_CONTRAST_15("ex_sm_15_office_02a", coordinates = Coordinates(-1392.626f, -480.4856f, 72.04212f)),//	Office
	OLD_SPICE_WARM_15("ex_sm_15_office_01a", coordinates = Coordinates(-1392.617f, -480.6363f, 72.04208f)),//	Office
	OLD_SPICE_CLASSICAL_15(
		"ex_sm_15_office_01b",
		coordinates = Coordinates(-1392.532f, -480.7649f, 72.04207f)
	),//	Office
	OLD_SPICE_VINTAGE_15("ex_sm_15_office_01c", coordinates = Coordinates(-1392.611f, -480.5562f, 72.04214f)),//	Office
	POWER_BROKER_ICE_15("ex_sm_15_office_03a", coordinates = Coordinates(-1392.563f, -480.549f, 72.0421f)),//	Office
	POWER_BROKER_CONVSERVATIVE_15(
		"ex_sm_15_office_03b",
		coordinates = Coordinates(-1392.528f, -480.475f, 72.04206f)
	),//	Office
	POWER_BROKER_POLISHED_15(
		"ex_sm_15_office_03c",
		coordinates = Coordinates(-1392.416f, -480.7485f, 72.04207f)
	),//	Office

	//	Clubhouse & Warehouses
//	Name	IPL	Vector3	Categories
	CLUBHOUSE_1(
		"bkr_biker_interior_placement_interior_0_biker_dlc_int_01_milo",
		coordinates = Coordinates(1107.04f, -3157.399f, -37.51859f)
	),//	Clubhouse
	CLUBHOUSE_2(
		"bkr_biker_interior_placement_interior_1_biker_dlc_int_02_milo",
		coordinates = Coordinates(998.4809f, -3164.711f, -38.90733f)
	),//	Clubhouse
	WAREHOUSE_1(
		"bkr_biker_interior_placement_interior_2_biker_dlc_int_ware01_milo",
		coordinates = Coordinates(1009.5f, -3196.6f, -38.99682f)
	),//	Warehouse
	WAREHOUSE_2(
		"bkr_biker_interior_placement_interior_3_biker_dlc_int_ware02_milo",
		coordinates = Coordinates(1051.491f, -3196.536f, -39.14842f)
	),//	Warehouse
	WAREHOUSE_3(
		"bkr_biker_interior_placement_interior_4_biker_dlc_int_ware03_milo",
		coordinates = Coordinates(1093.6f, -3196.6f, -38.99841f)
	),//	Warehouse
	WAREHOUSE_4(
		"bkr_biker_interior_placement_interior_5_biker_dlc_int_ware04_milo",
		coordinates = Coordinates(1121.897f, -3195.338f, -40.4025f)
	),//	Warehouse
	WAREHOUSE_5(
		"bkr_biker_interior_placement_interior_6_biker_dlc_int_ware05_milo",
		coordinates = Coordinates(1165f, -3196.6f, -39.01306f)
	), //	Warehouse
	WAREHOUSE_SMALL(
		"ex_exec_warehouse_placement_interior_1_int_warehouse_s_dlc_milo",
		coordinates = Coordinates(1094.988f, -3101.776f, -39.00363f)
	),//	Company Warehouse
	WAREHOUSE_MEDIUM(
		"ex_exec_warehouse_placement_interior_0_int_warehouse_m_dlc_milo",
		coordinates = Coordinates(1056.486f, -3105.724f, -39.00439f)
	),//	Company Warehouse
	WAREHOUSE_LARGE(
		"ex_exec_warehouse_placement_interior_2_int_warehouse_l_dlc_milo",
		coordinates = Coordinates(1006.967f, -3102.079f, -39.0035f)
	),//	Company Warehouse
	CARGARAGE(
		"imp_impexp_interior_placement_interior_1_impexp_intwaremed_milo_",
		coordinates = Coordinates(994.5925f, -3002.594f, -39.64699f)
	),//	Import Export DLC - Garage

	//	Special Locations
//	Name	IPL	Vector3	Categories
	NORMAL_CARGO_SHIP("cargoship", coordinates = Coordinates(-163.3628f, -2385.161f, 5.999994f)),//	Cargo Ship
	SUNKEN_CARGO_SHIP("sunkcargoship", coordinates = Coordinates(-163.3628f, -2385.161f, 5.999994f)),//	Cargo Ship
	BURNING_CARGO_SHIP("SUNK_SHIP_FIRE", coordinates = Coordinates(-163.3628f, -2385.161f, 5.999994f)),//	Cargo Ship
	RED_CARPET("redCarpet", coordinates = Coordinates(300.5927f, 300.5927f, 104.3776f)),//	Red Carpet
	REKT_STILTHOUSE_REBUILD(
		"DES_stilthouse_rebuild",
		coordinates = Coordinates(-1020.518f, 663.27f, 153.5167f)
	),//	Stilthouse
	REKT_STILTHOUSE_DESTROYED(
		"DES_StiltHouse_imapend",
		coordinates = REKT_STILTHOUSE_REBUILD.coordinates
	),//	Stilthouse
	UNION_DEPOSITORY("FINBANK", coordinates = Coordinates(2.6968f, -667.0166f, 16.13061f)),//	Bank
	TREVORS_TRAILER_DIRTY("TrevorsMP", coordinates = Coordinates(1975.552f, 3820.538f, 33.44833f)),//;	Trevor
	TREVORS_TRAILER_CLEAN("TrevorsTrailerTidy", coordinates = Coordinates(1975.552f, 3820.538f, 33.44833f)),//	Trevor
	STADIUM("SP1_10_real_interior", coordinates = Coordinates(-248.6731f, -2010.603f, 30.14562f)),//	Stadium
	MAX_RENDA_SHOP("refit_unload", coordinates = Coordinates(-585.8247f, -282.72f, 35.45475f)),//	Store
	JEWEL_STORE("post_hiest_unload", coordinates = Coordinates(-630.07f, -236.332f, 38.05704f)),//	Store
	FIB_LOBBY("FIBlobby", coordinates = Coordinates(110.4f, -744.2f, 45.7496f)),//	FIB


//	Special Locations (Multiple IPL per location)

	GUNRUNNING_HEIST_YACHT(
		"gr_heist_yacht2",
		"gr_heist_yacht2_bar",
		"gr_heist_yacht2_bedrm",
		"gr_heist_yacht2_bridge",
		"gr_heist_yacht2_enginrm",
		"gr_heist_yacht2_lounge",
		coordinates = Coordinates(-1418.21000000f, 6749.81000000f, 10.98968000f)
	),

	DIGNITY_HEIST_YACHT(
		"smboat",
		"smboat_lod",
		"hei_yacht_heist",
		"hei_yacht_heist_enginrm",
		"hei_yacht_heist_Lounge",
		"hei_yacht_heist_Bridge",
		"hei_yacht_heist_Bar",
		"hei_yacht_heist_Bedrm",
		"hei_yacht_heist_DistantLights",
		"hei_yacht_heist_LODLights",
		coordinates = Coordinates(-2027.946f, -1036.695f, 6.707587f)
	),

	DIGNITY_PARTY_YACHT(
		"smboat",
		"smboat_lod",
		"hei_yacht_heist",
		"hei_yacht_heist_enginrm",
		"hei_yacht_heist_Lounge",
		"hei_yacht_heist_Bridge",
		"hei_yacht_heist_Bar",
		"hei_yacht_heist_Bedrm",
		"hei_yacht_heist_DistantLights",
		"hei_yacht_heist_LODLights",
		coordinates = Coordinates(-2023.643f, -1038.119f, 5.576781f)
	),

	AIRCRAFT_CARRIER(
		"hei_carrier",
		"hei_carrier_DistantLights",
		"hei_Carrier_int1",
		"hei_Carrier_int2",
		"hei_Carrier_int3",
		"hei_Carrier_int4",
		"hei_Carrier_int5",
		"hei_Carrier_int6",
		"hei_carrier_LODLights",
		coordinates = Coordinates(3084.73f, -4770.709f, 15.26167f)
	),

	BRIDGE_TRAIN_CRASH(
		"canyonriver01_traincrash",
		"railing_end",
		coordinates = Coordinates(532.1309f, 4526.187f, 89.79387f)
	),

	// Cassidy Creek (need streamed content):
	BRIDGE_TRAIN_NORMAL(
		"canyonriver01",
		"railing_start",
		coordinates = Coordinates(-425.677f, 4433.404f, 27.3253f)
	),

	NORTH_YANKTON(
		"prologue01",
		"prologue01c",
		"prologue01d",
		"prologue01e",
		"prologue01f",
		"prologue01g",
		"prologue01h",
		"prologue01i",
		"prologue01j",
		"prologue01k",
		"prologue01z",
		"prologue02",
		"prologue03",
		"prologue03b",
		"prologue03_grv_dug",
		"prologue_grv_torch",
		"prologue04",
		"prologue04b",
		"prologue04_cover",
		"des_protree_end",
		"des_protree_start",
		"prologue05",
		"prologue05b",
		"prologue06",
		"prologue06b",
		"prologue06_int",
		"prologue06_pannel",
		"plg_occl_00",
		"prologue_occl",
		"prologue_DistantLights",
		"prologue_LODLights",
		"prologue_m2_door",
		"prologuerd",
		"prologuerdb",
		coordinates = Coordinates(3217.697f, -4834.826f, 111.8152f)
	),

//	ONeils Farm Burnt new Vector3(2469.03, 4955.278, 45.11892);

	ONEILS_FARM_BURNT(
		"farmint",
		"farm_burnt",
		"farm_burnt_props",
		"des_farmhs_endimap",
		"des_farmhs_end_occl",
		coordinates = Coordinates(2469.03f, 4955.278f, 45.11892f)
	),

	//	ONeils Farm
	ONEILS_FARM(
		"farm",
		"farm_props",
		"farm_int",
		coordinates = ONEILS_FARM_BURNT.coordinates
	),

	MORGUE(
		"coronertrash",
		"Coroner_Int_On",
		coordinates = Coordinates(275.446f, -1361.11f, 24.5378f)
	),

}