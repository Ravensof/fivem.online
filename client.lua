--
-- Created by IntelliJ IDEA.
-- User: drx_0
-- Date: 10.09.2018
-- Time: 20:08
-- To change this template use File | Settings | File Templates.
--

exports("emitNet", function(eventName, data)

    data = {
        ["data"] = data,
        ["numberOfPlayers"] = GetNumberOfPlayers(),
    }

    TriggerServerEvent(eventName, data)
end)

--exports("on", function(eventName, callback)
--    AddEventHandler(eventName, function(...)
--        callback(...)
--    end)
--end)
--
--exports("emit", function(eventName, data)
--    TriggerEvent(eventName, data)
--end)

