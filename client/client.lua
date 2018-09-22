--
-- Created by IntelliJ IDEA.
-- User: drx_0
-- Date: 10.09.2018
-- Time: 20:08
-- To change this template use File | Settings | File Templates.
--

exports("emitNet", function(eventName, ...)
    TriggerServerEvent(eventName, GetNumberOfPlayers(), ...)
end)

exports("onNui", function(eventName, callback)
    RegisterNUICallback(eventName, callback)
end)
