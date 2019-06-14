package online.fivem.client.modules.basics

import kotlinx.coroutines.Job
import online.fivem.client.common.AbstractClientModule
import online.fivem.client.extensions.request
import online.fivem.common.gtav.NativeIPl

class MapModule : AbstractClientModule() {

	override fun onStart(): Job? {
		listOf(
			NativeIPl.SHR_INT,
			NativeIPl.TREVORS_TRAILER_TRASH,
//			NativeInteriorPlace.TREVORS_TRAILER_TIDY,

			// Heist Jewel: -637.20159 -239.16250 38.1
			NativeIPl.JEWEL_STORE,

			// Max Renda: -585.8247, -282.72, 35.45475
			NativeIPl.MAX_RENDA_SHOP,

			// Heist Union Depository: 2.69689322, -667.0166, 16.1306286
			NativeIPl.FINBANK,

			// Morgue: 239.75195, -1360.64965, 39.53437
			NativeIPl.MORGUE,

			// Cluckin Bell: -146.3837, 6161.5, 30.2062
			NativeIPl.CS1_02_CF_ONMISSION1,
			NativeIPl.CS1_02_CF_ONMISSION2,
			NativeIPl.CS1_02_CF_ONMISSION3,
			NativeIPl.CS1_02_CF_ONMISSION4,

			// Grapeseed's farm: 2447.9, 4973.4, 47.7
			NativeIPl.GRAPESEEDS_FARM,

			// FIB lobby: 105.4557, -745.4835, 44.7548
			NativeIPl.FIB_LOBBY,

			// Billboard: iFruit
			NativeIPl.IFRUIT_BILLBOARD,
			NativeIPl.SC1_01_NEWBILL,
			NativeIPl.HW1_02_NEWBILL,
			NativeIPl.HW1_EMISSIVE_NEWBILL,
			NativeIPl.SC1_14_NEWBILL,
			NativeIPl.DT1_17_NEWBILL,

			// Lester's factory: 716.84, -962.05, 31.59
			NativeIPl.LESTERS_FACTORY,

			// Life Invader lobby: -1047.9, -233.0, 39.0
			NativeIPl.LIFE_INVADER_LOBBY,

			// Tunnels
			NativeIPl.V_TUNNEL_HOLE,

			// Carwash: 55.7, -1391.3, 30.5
			NativeIPl.CARWASH_WITH_SPINNERS,

			// Stadium "Fame or Shame": -248.49159240722656, -2010.509033203125, 34.57429885864258
			NativeIPl.STADIUM_FLAME_OR_SHAME,

			// House in Banham Canyon: -3086.428, 339.2523, 6.3717
			NativeIPl.HOUSE_IN_BANHAM_CANYON,

			// Garage in La Mesa (autoshop): 970.27453, -1826.56982, 31.11477
			NativeIPl.GARALE_IN_LA_MESA_AUTOSHOP,

			// Hill Valley church - Grave: -282.46380000, 2835.84500000, 55.91446000
			NativeIPl.HILL_VALLEY_CHURCH_GRAVE,

			// Lost's trailer park: 49.49379000, 3744.47200000, 46.38629000
			NativeIPl.LOSTS_TRAILER_PARK,

			// Lost safehouse: 984.1552, -95.3662, 74.50
			NativeIPl.LOSTS_SAFEHOUSE,

			// Raton Canyon river: -1652.83, 4445.28, 2.52
			NativeIPl.RATON_CANYON_RIVER,

			// Zancudo Gates (GTAO like): -1600.30100000, 2806.73100000, 18.79683000
			NativeIPl.ZANCUDO_GATES_GTAO_LIKE,

			// Pillbox hospital:
			NativeIPl.PILLBOX_HOSPITAL,

			// Josh's house: -1117.1632080078, 303.090698, 66.52217
			NativeIPl.JOSHS_HOUSE,

			// Zancudo River (need streamed content): 86.815, 3191.649, 30.463
			NativeIPl.ZANCUDO_RIVER,

			// Cassidy Creek (need streamed content): -425.677, 4433.404, 27.3253
			NativeIPl.BRIDGE_TRAIN_NORMAL,

			// Optional
			// Graffitis
			NativeIPl.ch3_rd2_bishopschickengraffiti, // 1861.28, 2402.11, 58.53
			NativeIPl.cs5_04_mazebillboardgraffiti, // 2697.32, 3162.18, 58.1
			NativeIPl.cs5_roads_ronoilgraffiti, // 2119.12, 3058.21, 53.25

			// Heist Carrier: 3082.3117 -4717.1191 15.2622
			NativeIPl.AIRCRAFT_CARRIER,

			// Heist Yatch: -2043.974,-1031.582, 11.981
			NativeIPl.DIGNITY_HEIST_YACHT,

			// Bunkers - Exteriors
			NativeIPl.ZANCUDO_BUNKER,
			NativeIPl.ROUTE68_BUNKER,
			NativeIPl.OILFIELD_SBUNKER,
			NativeIPl.DESERT_BUNKER,
			NativeIPl.SMOKETREE_BUNKER,
			NativeIPl.SCRAPYARD_BUNKER,
			NativeIPl.GRAPESEED_BUNKER,
			NativeIPl.PALLETO_BUNKER,
			NativeIPl.ROUTE1_BUNKER,
			NativeIPl.FARMHOUSE_BUNKER,
			NativeIPl.RATON_CANYON_BUNKER,

			// Bunkers - Interior: 892.6384, -3245.8664, -98.2645
			NativeIPl.BUNKERS_INTERIOR,

			// Bahama Mamas: -1388.0013, -618.41967, 30.819599
			NativeIPl.BAHAMA_MAMAS,

			// Red Carpet: 300.5927, 199.7589, 104.3776
			NativeIPl.RED_CARPET,

			// UFO
			// Zancudo: -2051.99463, 3237.05835, 1456.97021
			// Hippie base: 2490.47729, 3774.84351, 2414.035
			// Chiliad: 501.52880000, 5593.86500000, 796.23250000
			// ufo("ufo"),
			// ufo_eye("ufo_eye"),
			// ufo_lod("ufo_lod"),

			// North Yankton: 3217.697, -4834.826, 111.8152
			NativeIPl.NORTH_YANKTON,

			// CEO Offices :
			// Arcadius Business Centre
			NativeIPl.EXECUTIVE_RICH,    // Executive Rich
			NativeIPl.EXECUTIVE_COOL,    // Executive Cool
			NativeIPl.EXECUTIVE_CONTRAST,    // Executive Contrast
			NativeIPl.OLD_SPICE_WARM,    // Old Spice Warm
			NativeIPl.OLD_SPICE_CLASSICAL,    // Old Spice Classical
			NativeIPl.OLD_SPICE_VINTAGE,    // Old Spice Vintage
			NativeIPl.POWER_BROKER_ICE,    // Power Broker Ice
			NativeIPl.POWER_BROKER_CONSERVATIVE,    // Power Broker Conservative
			NativeIPl.POWER_BROKER_POLISHED,    // Power Broker Polished

			// Maze Bank Building
			NativeIPl.EXECUTIVE_RICH_11,    // Executive Rich
			NativeIPl.EXECUTIVE_COOL_11,    // Executive Cool
			NativeIPl.EXECUTIVE_CONTRAST_11,    // Executive Contrast
			NativeIPl.OLD_SPICE_WARM_11,    // Old Spice Warm
			NativeIPl.OLD_SPICE_CLASSICAL_11,    // Old Spice Classical
			NativeIPl.OLD_SPICE_VINTAGE_11,    // Old Spice Vintage
			NativeIPl.POWER_BROKER_ICE_11,    // Power Broker Ice
			NativeIPl.POWER_BROKER_CONSERVATIVE_11,    // Power Broker Conservative
			NativeIPl.POWER_BROKER_POLISHED_11,    // Power Broker Polished

			// Lom Bank
			NativeIPl.EXECUTIVE_RICH_13,    // Executive Rich
			NativeIPl.EXECUTIVE_COOL_13,    // Executive Cool
			NativeIPl.EXECUTIVE_CONTRAST_13,    // Executive Contrast
			NativeIPl.OLD_SPICE_WARM_13,    // Old Spice Warm
			NativeIPl.OLD_SPICE_CLASSICAL_13,    // Old Spice Classical
			NativeIPl.OLD_SPICE_VINTAGE_13,    // Old Spice Vintage
			NativeIPl.POWER_BROKER_ICE_13,    // Power Broker Ice
			NativeIPl.POWER_BROKER_CONSERVATIVE_13,    // Power Broker Conservative
			NativeIPl.POWER_BROKER_POLISHED_13,    // Power Broker Polished

			// Maze Bank West
			NativeIPl.EXECUTIVE_RICH_15,    // Executive Rich
			NativeIPl.EXECUTIVE_COOL_15,    // Executive Cool
			NativeIPl.EXECUTIVE_CONTRAST_15,    // Executive Contrast
			NativeIPl.OLD_SPICE_WARM_15,    // Old Spice Warm
			NativeIPl.OLD_SPICE_CLASSICAL_15,    // Old Spice Classical
			NativeIPl.OLD_SPICE_VINTAGE_15,    // Old Spice Vintage
			NativeIPl.POWER_BROKER_ICE_15,    // Power Broker Ice
			NativeIPl.POWER_BROKER_CONVSERVATIVE_15,    // Power Broker Convservative
			NativeIPl.POWER_BROKER_POLISHED_15,    // Power Broker Polished

			// Biker
			NativeIPl.BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_0_BIKER_DLC_INT_01_MILO,
			NativeIPl.BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_1_BIKER_DLC_INT_02_MILO,

			NativeIPl.BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE01_MILO,
			NativeIPl.BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE02_MILO,
			NativeIPl.BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE03_MILO,
			NativeIPl.BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE04_MILO,
			NativeIPl.BKR_BIKER_INTERIOR_PLACEMENT_INTERIOR_2_BIKER_DLC_INT_WARE05_MILO,

			NativeIPl.EX_EXEC_WAREHOUSE_PLACEMENT_INTERIOR_0_INT_WAREHOUSE_M_DLC_MILO,
			NativeIPl.EX_EXEC_WAREHOUSE_PLACEMENT_INTERIOR_1_INT_WAREHOUSE_S_DLC_MILO,
			NativeIPl.EX_EXEC_WAREHOUSE_PLACEMENT_INTERIOR_2_INT_WAREHOUSE_L_DLC_MILO,

			// IMPORT/EXPORT
			NativeIPl.imp_dt1_02_modgarage,
			NativeIPl.imp_dt1_02_cargarage_a,
			NativeIPl.imp_dt1_02_cargarage_b,
			NativeIPl.imp_dt1_02_cargarage_c,

			NativeIPl.imp_dt1_11_modgarage,
			NativeIPl.imp_dt1_11_cargarage_a,
			NativeIPl.imp_dt1_11_cargarage_b,
			NativeIPl.imp_dt1_11_cargarage_c,

			NativeIPl.imp_sm_13_modgarage,
			NativeIPl.imp_sm_13_cargarage_a,
			NativeIPl.imp_sm_13_cargarage_b,
			NativeIPl.imp_sm_13_cargarage_c,

			NativeIPl.imp_sm_15_modgarage,
			NativeIPl.imp_sm_15_cargarage_a,
			NativeIPl.imp_sm_15_cargarage_b,
			NativeIPl.imp_sm_15_cargarage_c,

			NativeIPl.imp_impexp_interior_placement,
			NativeIPl.imp_impexp_interior_placement_interior_0_impexp_int_01_milo_,
			NativeIPl.imp_impexp_interior_placement_interior_3_impexp_int_02_milo_,
			NativeIPl.imp_impexp_interior_placement_interior_1_impexp_intwaremed_milo_,
			NativeIPl.imp_impexp_interior_placement_interior_2_imptexp_mod_int_01_milo_
		).forEach {
			it.request()
		}

		return super.onStart()
	}
}