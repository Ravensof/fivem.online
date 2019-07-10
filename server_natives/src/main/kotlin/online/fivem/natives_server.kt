@file:Suppress(
	"INTERFACE_WITH_SUPERCLASS",
	"OVERRIDING_FINAL_MEMBER",
	"RETURN_TYPE_MISMATCH_ON_OVERRIDE",
	"CONFLICTING_OVERLOADS",
	"EXTERNAL_DELEGATION",
	"NESTED_CLASS_IN_EXTERNAL_INTERFACE"
)

package online.fivem

/*
^declare function(.*);$
internal external fun$1

: string
: String


: (\S+ (\| [^\s,]+)+)
: Any /*$1*/
*/


/**
 * Returns whether or not the specified player has enough information to start a commerce session for.
 * @param playerSrc The player handle
 * @return True or false.
 */
internal external fun CanPlayerStartCommerceSession(playerSrc: String): Number

/**
 * Cancels the currently executing event.
 */
internal external fun CancelEvent(): Unit

/**
 * thisScriptCheck - can be destroyed if it belongs to the calling script.
 */
internal external fun CreateVehicle(
	modelHash: Any /*String | number*/,
	x: Number,
	y: Number,
	z: Number,
	heading: Number,
	isNetwork: Boolean,
	thisScriptCheck: Boolean
): Unit

internal external fun DeleteFunctionReference(referenceIdentity: String): Unit

internal external fun DoesEntityExist(entity: Number): Number

/**
 * Requests whether or not the player owns the specified SKU.
 * @param playerSrc The player handle
 * @param skuId The ID of the SKU.
 * @return A boolean.
 */
internal external fun DoesPlayerOwnSku(playerSrc: String, skuId: Number): Number

internal external fun DropPlayer(playerSrc: String, reason: String): Unit

internal external fun DuplicateFunctionReference(referenceIdentity: String): String

internal external fun EnableEnhancedHostSupport(enabled: Boolean): Unit

internal external fun ExecuteCommand(commandString: String): Unit

internal external fun FlagServerAsPrivate(private_: Boolean): Unit

/**
 * No, this should be called SET_ENTITY_KINEMATIC. It does more than just "freeze" it's position.
 * ^Rockstar Devs named it like that, Now cry about it.
 */
internal external fun FreezeEntityPosition(entity: Number, toggle: Boolean): Unit

internal external fun GetConvar(varName: String, default_: String): String

internal external fun GetConvarInt(varName: String, default_: Number): Number

/**
 * Returns the name of the currently executing resource.
 * @return The name of the resource.
 */
internal external fun GetCurrentResourceName(): String

internal external fun GetEntityCoords(entity: Number): Array<Number>

internal external fun GetEntityHeading(entity: Number): Number

internal external fun GetEntityModel(entity: Number): Number

internal external fun GetEntityPopulationType(entity: Number): Number

internal external fun GetEntityRotation(entity: Number): Array<Number>

internal external fun GetEntityRotationVelocity(entity: Number): Array<Number>

internal external fun GetEntityScript(entity: Number): String

internal external fun GetEntityType(entity: Number): Number

internal external fun GetEntityVelocity(entity: Number): Array<Number>

/**
 * Gets the current game timer in milliseconds.
 * @return The game time.
 */
internal external fun GetGameTimer(): Number

/**
 * This native converts the passed string to a hash.
 */
internal external fun GetHashKey(model: String): Number

internal external fun GetHostId(): String

internal external fun GetInstanceId(): Number

internal external fun GetInvokingResource(): String

internal external fun GetNumPlayerIdentifiers(playerSrc: String): Number

internal external fun GetNumPlayerIndices(): Number

/**
 * Gets the amount of metadata values with the specified key existing in the specified resource's manifest.
 * See also: [Resource manifest](https://docs.fivem.net/resources/manifest/)
 * @param resourceName The resource name.
 * @param metadataKey The key to look up in the resource manifest.
 */
internal external fun GetNumResourceMetadata(resourceName: String, metadataKey: String): Number

internal external fun GetNumResources(): Number

internal external fun GetPasswordHash(password: String): String

internal external fun GetPlayerEndpoint(playerSrc: String): String

internal external fun GetPlayerFromIndex(index: Number): String

internal external fun GetPlayerGuid(playerSrc: String): String

internal external fun GetPlayerIdentifier(playerSrc: String, identifier: Number): String

internal external fun GetPlayerLastMsg(playerSrc: String): Number

internal external fun GetPlayerName(playerSrc: String): String

internal external fun GetPlayerPed(playerSrc: String): Number

internal external fun GetPlayerPing(playerSrc: String): Number

/**
 * Returns all commands that are registered in the command system.
 * The data returned adheres to the following layout:
 * ```
 * [
 * {
 * "name": "cmdlist"
 * },
 * {
 * "name": "command1"
 * }
 * ]
 * ```
 * @return An object containing registered commands.
 */
internal external fun GetRegisteredCommands(): Any

internal external fun GetResourceByFindIndex(findIndex: Number): String

/**
 * Gets the metadata value at a specified key/index from a resource's manifest.
 * See also: [Resource manifest](https://docs.fivem.net/resources/manifest/)
 * @param resourceName The resource name.
 * @param metadataKey The key in the resource manifest.
 * @param index The value index, in a range from [0..GET_NUM_RESOURCE_METDATA-1].
 */
internal external fun GetResourceMetadata(resourceName: String, metadataKey: String, index: Number): String

/**
 * Returns the physical on-disk path of the specified resource.
 * @param resourceName The name of the resource.
 * @return The resource directory name, possibly without trailing slash.
 */
internal external fun GetResourcePath(resourceName: String): String

/**
 * Returns the current state of the specified resource.
 * @param resourceName The name of the resource.
 * @return The resource state. One of `"missing", "started", "starting", "stopped", "stopping", "uninitialized" or "unknown"`.
 */
internal external fun GetResourceState(resourceName: String): String

internal external fun InvokeFunctionReference(
	referenceIdentity: String,
	argsSerialized: String,
	argsLength: Number,
	retvalLength: Number
): String

internal external fun IsAceAllowed(_object: String): Number

/**
 * Gets whether or not this is the CitizenFX server.
 * @return A boolean value.
 */
internal external fun IsDuplicityVersion(): Number

internal external fun IsPlayerAceAllowed(playerSrc: String, _object: String): Number

/**
 * Requests whether or not the commerce data for the specified player has loaded.
 * @param playerSrc The player handle
 * @return A boolean.
 */
internal external fun IsPlayerCommerceInfoLoaded(playerSrc: String): Number

internal external fun IsPrincipalAceAllowed(principal: String, _object: String): Number

/**
 * Requests the commerce data for the specified player, including the owned SKUs. Use `IS_PLAYER_COMMERCE_INFO_LOADED` to check if it has loaded.
 * @param playerSrc The player handle
 */
internal external fun LoadPlayerCommerceData(playerSrc: String): Unit

/**
 * Reads the contents of a text file in a specified resource.
 * If executed on the client, this file has to be included in `files` in the resource manifest.
 * Example: `local data = LoadResourceFile("devtools", "data.json")`
 * @param resourceName The resource name.
 * @param fileName The file in the resource.
 * @return The file contents
 */
internal external fun LoadResourceFile(resourceName: String, fileName: String): String

internal external fun NetworkGetEntityFromNetworkId(netId: Number): Number

/**
 * Returns the owner ID of the specified entity.
 * @param entity The entity to get the owner for.
 * @return On the server, the server ID of the entity owner. On the client, returns the player/slot ID of the entity owner.
 */
internal external fun NetworkGetEntityOwner(entity: Number): Number

internal external fun NetworkGetNetworkIdFromEntity(entity: Number): Number

internal external fun PerformHttpRequestInternal(requestData: String, requestDataLength: Number): Number

/**
 * Registered commands can be executed by entering them in the client console (this works for client side and server side registered commands). Or by entering them in the server console/through an RCON client (only works for server side registered commands). Or if you use a supported chat resource, like the default one provided in the cfx-server-data repository, then you can enter the command in chat by prefixing it with a `/`.
 * Commands registered using this function can also be executed by resources, using the [`ExecuteCommand` native](#_0x561C060B).
 * The restricted bool is not used on the client side. Permissions can only be checked on the server side, so if you want to limit your command with an ace permission automatically, make it a server command (by registering it in a server script).
 * **Example result**:
 * ![](https://i.imgur.com/TaCnG09.png)
 * @param commandName The command you want to register.
 * @param handler A handler function that gets called whenever the command is executed.
 * @param restricted If this is a server command and you set this to true, then players will need the command.yourCommandName ace permission to execute this command.
 */
internal external fun RegisterCommand(commandName: String, handler: Any /* Function */, restricted: Boolean): Unit

/**
 * An internal function which allows the current resource's HLL script runtimes to receive state for the specified event.
 * @param eventName An event name, or "\*" to disable HLL event filtering for this resource.
 */
internal external fun RegisterResourceAsEventHandler(eventName: String): Unit

/**
 * **Experimental**: This native may be altered or removed in future versions of CitizenFX without warning.
 * Registers a cached resource asset with the resource system, similar to the automatic scanning of the `stream/` folder.
 * @param resourceName The resource to add the asset to.
 * @param fileName A file name in the resource.
 * @return A cache string to pass to `REGISTER_STREAMING_FILE_FROM_CACHE` on the client.
 */
internal external fun RegisterResourceAsset(resourceName: String, fileName: String): String

/**
 * Registers a build task factory for resources.
 * The function should return an object (msgpack map) with the following fields:
 * ```
 * {
 * // returns whether the specific resource should be built
 * shouldBuild = func(resourceName: String): bool,
 * // asynchronously start building the specific resource.
 * // call cb when completed
 * build = func(resourceName: String, cb: func(success: bool, status: String): Unit): Unit
 * }
 * ```
 * @param factoryId The identifier for the build task.
 * @param factoryFn The factory function.
 */
internal external fun RegisterResourceBuildTaskFactory(factoryId: String, factoryFn: Any /* Function */): Unit

/**
 * Requests the specified player to buy the passed SKU. This'll pop up a prompt on the client, which upon acceptance
 * will open the browser prompting further purchase details.
 * @param playerSrc The player handle
 * @param skuId The ID of the SKU.
 */
internal external fun RequestPlayerCommerceSession(playerSrc: String, skuId: Number): Unit

/**
 * Writes the specified data to a file in the specified resource.
 * Using a length of `-1` will automatically detect the length assuming the data is a C string.
 * @param resourceName The name of the resource.
 * @param fileName The name of the file.
 * @param data The data to write to the file.
 * @param dataLength The length of the written data.
 * @return A value indicating if the write succeeded.
 */
internal external fun SaveResourceFile(resourceName: String, fileName: String, data: String, dataLength: Number): Number

internal external fun SetConvar(varName: String, value: String): Unit

internal external fun SetConvarReplicated(varName: String, value: String): Unit

internal external fun SetConvarServerInfo(varName: String, value: String): Unit

/**
 * p7 is always 1 in the scripts. Set to 1, an area around the destination coords for the moved entity is cleared from other entities.
 * Often ends with 1, 0, 0, 1); in the scripts. It works.
 * Axis - Invert Axis Flags
 */
internal external fun SetEntityCoords(
	entity: Number,
	xPos: Number,
	yPos: Number,
	zPos: Number,
	xAxis: Boolean,
	yAxis: Boolean,
	zAxis: Boolean,
	clearArea: Boolean
): Unit

internal external fun SetEntityHeading(entity: Number, heading: Number): Unit

/**
 * rotationOrder refers to the order yaw pitch roll is applied
 * value ranges from 0 to 5. What you use for rotationOrder when setting must be the same as rotationOrder when getting the rotation.
 * Unsure what value corresponds to what rotation order, more testing will be needed for that.
 * For the most part R* uses 1 or 2 as the order.
 * p5 is usually set as true
 */
internal external fun SetEntityRotation(
	entity: Number,
	pitch: Number,
	roll: Number,
	yaw: Number,
	rotationOrder: Number,
	p5: Boolean
): Unit

/**
 * Note that the third parameter(denoted as z) is "up and down" with positive ment.
 */
internal external fun SetEntityVelocity(entity: Number, x: Number, y: Number, z: Number): Unit

internal external fun SetGameType(gametypeName: String): Unit

internal external fun SetHttpHandler(handler: Any /* Function */): Unit

internal external fun SetMapName(mapName: String): Unit

/**
 * Ped: The ped to warp.
 * vehicle: The vehicle to warp the ped into.
 * Seat_Index: [-1 is driver seat, -2 first free passenger seat]
 * Moreinfo of Seat Index
 * DriverSeat = -1
 * Passenger = 0
 * Left Rear = 1
 * RightRear = 2
 */
internal external fun SetPedIntoVehicle(ped: Number, vehicle: Number, seatIndex: Number): Unit

/**
 * Call SET_PLAYER_WANTED_LEVEL_NOW for immediate effect
 * wantedLevel is an integer value representing 0 to 5 stars even though the game supports the 6th wanted level but no police will appear since no definitions are present for it in the game files
 * disableNoMission-  Disables When Off Mission- appears to always be false
 */
internal external fun SetPlayerWantedLevel(player: Number, wantedLevel: Number, disableNoMission: Boolean): Unit

/**
 * colorPrimary & colorSecondary are the paint index for the vehicle.
 * For a list of valid paint indexes, view: pastebin.com/pwHci0xK
 * -------------------------------------------------------------------------
 * Use this to get the number of color indices: pastebin.com/RQEeqTSM
 * Note: minimum color index is 0, maximum color index is (numColorIndices - 1)
 */
internal external fun SetVehicleColours(vehicle: Number, colorPrimary: Number, colorSecondary: Number): Unit

internal external fun StartResource(resourceName: String): Number

internal external fun StopResource(resourceName: String): Number

internal external fun TempBanPlayer(playerSrc: String, reason: String): Unit

/**
 * The backing function for TriggerClientEvent.
 */
internal external fun TriggerClientEventInternal(
	eventName: String,
	eventTarget: String,
	eventPayload: String,
	payloadLength: Number
): Unit

/**
 * The backing function for TriggerEvent.
 */
internal external fun TriggerEventInternal(eventName: String, eventPayload: String, payloadLength: Number): Unit

internal external fun VerifyPasswordHash(password: String, hash: String): Number

/**
 * Returns whether or not the currently executing event was canceled.
 * @return A boolean.
 */
internal external fun WasEventCanceled(): Number

