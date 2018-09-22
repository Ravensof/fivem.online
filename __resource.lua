resource_manifest_version '05cfa83c-a124-4cfa-a768-c24a5811d8f9'

resource_type 'data' {
    name = 'test'
}

files {
    --    'external/Base64.min.js',-- уже считается загруженным в client_scripts
    --
    --    'out/production/web/lib/kotlin.js',
    --    'out/production/universal/universal.js',
    'out/production/web/web.js',
    'web/index.html'
}

ui_page "web/index.html"

client_scripts {
    'client/client.lua',

    'external/Base64.min.js',

    'out/production/client/lib/kotlin.js',
    'out/production/universal/universal.js',
    'out/production/fivem/fivem.js',
    'out/production/client/client.js'
}

server_scripts {
    'server/server.lua',

    'external/Base64.min.js',

    'out/production/server/lib/kotlin.js',
    'out/production/universal/universal.js',
    'out/production/fivem/fivem.js',
    'out/production/server/server.js'
}

