@file:Suppress("SpellCheckingInspection")

package online.fivem.common.gtav

enum class NativeWeapon(val code: String, val hash: Float) {
//	----------------Weapons----------------

	WEAPON_UNARMED("WEAPON_UNARMED", 2725352035f),
	WEAPON_ANIMAL("WEAPON_ANIMAL", 4194021054f),
	WEAPON_COUGAR("WEAPON_COUGAR", 148160082f),
	WEAPON_KNIFE("WEAPON_KNIFE", 2578778090f),
	WEAPON_NIGHTSTICK("WEAPON_NIGHTSTICK", 1737195953f),
	WEAPON_HAMMER("WEAPON_HAMMER", 1317494643f),
	WEAPON_BAT("WEAPON_BAT", 2508868239f),
	WEAPON_GOLFCLUB("WEAPON_GOLFCLUB", 1141786504f),
	WEAPON_CROWBAR("WEAPON_CROWBAR", 2227010557f),
	WEAPON_PISTOL("WEAPON_PISTOL", 453432689f),
	WEAPON_COMBATPISTOL("WEAPON_COMBATPISTOL", 1593441988f),
	WEAPON_APPISTOL("WEAPON_APPISTOL", 584646201f),
	WEAPON_PISTOL50("WEAPON_PISTOL50", 2578377531f),
	WEAPON_MICROSMG("WEAPON_MICROSMG", 324215364f),
	WEAPON_SMG("WEAPON_SMG", 736523883f),
	WEAPON_ASSAULTSMG("WEAPON_ASSAULTSMG", 4024951519f),
	WEAPON_ASSAULTRIFLE("WEAPON_ASSAULTRIFLE", 3220176749f),
	WEAPON_CARBINERIFLE("WEAPON_CARBINERIFLE", 2210333304f),
	WEAPON_ADVANCEDRIFLE("WEAPON_ADVANCEDRIFLE", 2937143193f),
	WEAPON_MG("WEAPON_MG", 2634544996f),
	WEAPON_COMBATMG("WEAPON_COMBATMG", 2144741730f),
	WEAPON_PUMPSHOTGUN("WEAPON_PUMPSHOTGUN", 487013001f),
	WEAPON_SAWNOFFSHOTGUN("WEAPON_SAWNOFFSHOTGUN", 2017895192f),
	WEAPON_ASSAULTSHOTGUN("WEAPON_ASSAULTSHOTGUN", 3800352039f),
	WEAPON_BULLPUPSHOTGUN("WEAPON_BULLPUPSHOTGUN", 2640438543f),
	WEAPON_STUNGUN("WEAPON_STUNGUN", 911657153f),
	WEAPON_SNIPERRIFLE("WEAPON_SNIPERRIFLE", 100416529f),
	WEAPON_HEAVYSNIPER("WEAPON_HEAVYSNIPER", 205991906f),
	WEAPON_REMOTESNIPER("WEAPON_REMOTESNIPER", 856002082f),
	WEAPON_GRENADELAUNCHER("WEAPON_GRENADELAUNCHER", 2726580491f),
	WEAPON_GRENADELAUNCHER_SMOKE("WEAPON_GRENADELAUNCHER_SMOKE", 1305664598f),
	WEAPON_RPG("WEAPON_RPG", 2982836145f),
	WEAPON_PASSENGER_ROCKET("WEAPON_PASSENGER_ROCKET", 375527679f),
	WEAPON_AIRSTRIKE_ROCKET("WEAPON_AIRSTRIKE_ROCKET", 324506233f),
	WEAPON_STINGER("WEAPON_STINGER", 1752584910f),
	WEAPON_MINIGUN("WEAPON_MINIGUN", 1119849093f),
	WEAPON_GRENADE("WEAPON_GRENADE", 2481070269f),
	WEAPON_STICKYBOMB("WEAPON_STICKYBOMB", 741814745f),
	WEAPON_SMOKEGRENADE("WEAPON_SMOKEGRENADE", 4256991824f),
	WEAPON_BZGAS("WEAPON_BZGAS", 2694266206f),
	WEAPON_MOLOTOV("WEAPON_MOLOTOV", 615608432f),
	WEAPON_FIREEXTINGUISHER("WEAPON_FIREEXTINGUISHER", 101631238f),
	WEAPON_PETROLCAN("WEAPON_PETROLCAN", 883325847f),
	WEAPON_DIGISCANNER("WEAPON_DIGISCANNER", 4256881901f),
	WEAPON_BRIEFCASE("WEAPON_BRIEFCASE", 2294779575f),
	WEAPON_BRIEFCASE_02("WEAPON_BRIEFCASE_02", 28811031f),
	WEAPON_BALL("WEAPON_BALL", 600439132f),
	WEAPON_FLARE("WEAPON_FLARE", 1233104067f),
	WEAPON_VEHICLE_ROCKET("WEAPON_VEHICLE_ROCKET", 3204302209f),
	WEAPON_BARBED_WIRE("WEAPON_BARBED_WIRE", 1223143800f),
	WEAPON_DROWNING("WEAPON_DROWNING", 4284007675f),
	WEAPON_DROWNING_IN_VEHICLE("WEAPON_DROWNING_IN_VEHICLE", 1936677264f),
	WEAPON_BLEEDING("WEAPON_BLEEDING", 2339582971f),
	WEAPON_ELECTRIC_FENCE("WEAPON_ELECTRIC_FENCE", 2461879995f),
	WEAPON_EXPLOSION("WEAPON_EXPLOSION", 539292904f),
	WEAPON_FALL("WEAPON_FALL", 3452007600f),
	WEAPON_EXHAUSTION("WEAPON_EXHAUSTION", 910830060f),
	WEAPON_HIT_BY_WATER_CANNON("WEAPON_HIT_BY_WATER_CANNON", 3425972830f),
	WEAPON_RAMMED_BY_CAR("WEAPON_RAMMED_BY_CAR", 133987706f),
	WEAPON_RUN_OVER_BY_CAR("WEAPON_RUN_OVER_BY_CAR", 2741846334f),
	WEAPON_HELI_CRASH("WEAPON_HELI_CRASH", 341774354f),
	WEAPON_FIRE("WEAPON_FIRE", 3750660587f),

//	----------------DLC Weapons----------------

	//	Beach Bum Update:
//	------------------------------------
	WEAPON_SNSPISTOL("WEAPON_SNSPISTOL", 3218215474f),
	WEAPON_BOTTLE("WEAPON_BOTTLE", 4192643659f),

	//	Valentines Day Massacre Special:
//	------------------------------------
	WEAPON_GUSENBERG("WEAPON_GUSENBERG", 1627465347f),

	//	Business Update:
//	------------------------------------
	WEAPON_SPECIALCARBINE("WEAPON_SPECIALCARBINE", 3231910285f),
	WEAPON_HEAVYPISTOL("WEAPON_HEAVYPISTOL", 3523564046f),

	//	High Life Update:
//	------------------------------------
	WEAPON_BULLPUPRIFLE("WEAPON_BULLPUPRIFLE", 2132975508f),

	//	"I'm Not a Hipster" Update:
//	------------------------------------
	WEAPON_DAGGER("WEAPON_DAGGER", 2460120199f),
	WEAPON_VINTAGEPISTOL("WEAPON_VINTAGEPISTOL", 137902532f),

	//	Independence Day Special:
//	------------------------------------
	WEAPON_FIREWORK("WEAPON_FIREWORK", 2138347493f),
	WEAPON_MUSKET("WEAPON_MUSKET", 2828843422f),

	//	Last Team Standing Update:
//	------------------------------------
	WEAPON_HEAVYSHOTGUN("WEAPON_HEAVYSHOTGUN", 984333226f),
	WEAPON_MARKSMANRIFLE("WEAPON_MARKSMANRIFLE", 3342088282f),

	//	Festive Surprise:
//	------------------------------------
	WEAPON_HOMINGLAUNCHER("WEAPON_HOMINGLAUNCHER", 1672152130f),
	WEAPON_PROXMINE("WEAPON_PROXMINE", 2874559379f),
	WEAPON_SNOWBALL("WEAPON_SNOWBALL", 126349499f),

	//	Heists Update:
//	------------------------------------
	WEAPON_FLAREGUN("WEAPON_FLAREGUN", 1198879012f),
	WEAPON_GARBAGEBAG("WEAPON_GARBAGEBAG", 3794977420f),
	WEAPON_HANDCUFFS("WEAPON_HANDCUFFS", 3494679629f),

	//	Ill-Gotten Gains Part 1:
//	------------------------------------
	WEAPON_COMBATPDW("WEAPON_COMBATPDW", 171789620f),

	//	Ill-Gotten Gains Part 2:
//	------------------------------------
	WEAPON_MARKSMANPISTOL("WEAPON_MARKSMANPISTOL", 3696079510f),
	WEAPON_KNUCKLE("WEAPON_KNUCKLE", 3638508604f),

	//	Enhanced edition:
//	------------------------------------
	WEAPON_HATCHET("WEAPON_HATCHET", 4191993645f),
	WEAPON_RAILGUN("WEAPON_RAILGUN", 1834241177f),

	//	Lowriders:
//	------------------------------------
	WEAPON_MACHETE("WEAPON_MACHETE", 3713923289f),
	WEAPON_MACHINEPISTOL("WEAPON_MACHINEPISTOL", 3675956304f),

	//	Executives and Other Criminals:
//	------------------------------------
	WEAPON_AIR_DEFENCE_GUN("WEAPON_AIR_DEFENCE_GUN", 738733437f),
	WEAPON_SWITCHBLADE("WEAPON_SWITCHBLADE", 3756226112f),
	WEAPON_REVOLVER("WEAPON_REVOLVER", 3249783761f),

	//	Lowriders: Custom Classics:
//	------------------------------------
	WEAPON_DBSHOTGUN("WEAPON_DBSHOTGUN", 4019527611f),
	WEAPON_COMPACTRIFLE("WEAPON_COMPACTRIFLE", 1649403952f),

	//	Bikers:
//	------------------------------------
	WEAPON_AUTOSHOTGUN("WEAPON_AUTOSHOTGUN", 317205821f),
	WEAPON_BATTLEAXE("WEAPON_BATTLEAXE", 3441901897f),
	WEAPON_COMPACTLAUNCHER("WEAPON_COMPACTLAUNCHER", 125959754f),
	WEAPON_MINISMG("WEAPON_MINISMG", 3173288789f),
	WEAPON_PIPEBOMB("WEAPON_PIPEBOMB", 3125143736f),
	WEAPON_POOLCUE("WEAPON_POOLCUE", 2484171525f),
	WEAPON_WRENCH("WEAPON_WRENCH", 419712736f),

	//	----------------Gadgets----------------
	GADGET_NIGHTVISION("GADGET_NIGHTVISION", 2803906140f),
	GADGET_PARACHUTE("GADGET_PARACHUTE", 4222310262f),

//	----------------Explosions----------------

	GRENADE("GRENADE", 0f),
	GRENADELAUNCHER("GRENADELAUNCHER", 1f),
	STICKYBOMB("STICKYBOMB", 2f),
	MOLOTOV("MOLOTOV", 3f),
	ROCKET("ROCKET", 4f),
	TANKSHELL("TANKSHELL", 5f),
	HI_OCTANE("HI_OCTANE", 6f),
	CAR("CAR", 7f),
	PLANE("PLANE", 8f),
	PETROL_PUMP("PETROL_PUMP", 9f),
	BIKE("BIKE", 10f),
	DIR_STEAM("DIR_STEAM", 11f),
	DIR_FLAME("DIR_FLAME", 12f),
	DIR_WATER_HYDRANT("DIR_WATER_HYDRANT", 13f),
	DIR_GAS_CANISTER("DIR_GAS_CANISTER", 14f),
	BOAT("BOAT", 15f),
	SHIP_DESTROY("SHIP_DESTROY", 16f),
	TRUCK("TRUCK", 17f),
	BULLET("BULLET", 18f),
	SMOKEGRENADELAUNCHER("SMOKEGRENADELAUNCHER", 19f),
	SMOKEGRENADE("SMOKEGRENADE", 20f),
	BZGAS("BZGAS", 21f),
	FLARE("FLARE", 22f),
	GAS_CANISTER("GAS_CANISTER", 23f),
	EXTINGUISHER("EXTINGUISHER", 24f),
	PROGRAMMABLEAR("PROGRAMMABLEAR", 25f),
	TRAIN("TRAIN", 26f),
	BARREL("BARREL", 27f),
	PROPANE("PROPANE", 28f),
	BLIMP("BLIMP", 29f),
	DIR_FLAME_EXPLODE("DIR_FLAME_EXPLODE", 30f),
	TANKER("TANKER", 31f),
	PLANE_ROCKET("PLANE_ROCKET", 32f),
	VEHICLE_BULLET("VEHICLE_BULLET", 33f),
	GAS_TANK("GAS_TANK", 34f),
	FIREWORK("FIREWORK", 35f),
	SNOWBALL("SNOWBALL", 36f),
	PROXMINE("PROXMINE", 37f),
	VALKYRIE_CANNON("VALKYRIE_CANNON", 38f),

}