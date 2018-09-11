--
-- Created by IntelliJ IDEA.
-- User: drx_0
-- Date: 09.09.2018
-- Time: 15:33
-- To change this template use File | Settings | File Templates.
--
--TriggerEvent('log', {[''] = ''})


exports("performHttpRequest", function(url, callback, httpMethod, data, headers)
    PerformHttpRequest(url, callback, httpMethod, data, headers)
end)

exports("onNet", function(eventName, callback)
    RegisterServerEvent(eventName)

    AddEventHandler(eventName, function(data)
        callback(source, data["data"], data["numberOfPlayers"])
    end)
end)

exports("on", function(eventName, callback)
    AddEventHandler(eventName, function(...)
        callback(source, ...)
    end)
end)
