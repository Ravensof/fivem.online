resource_manifest_version '05cfa83c-a124-4cfa-a768-c24a5811d8f9'

resource_type 'data' {
    name = 'test'
}

client_scripts {
    'client.lua',

    'out/production/client/lib/kotlin.js',
    'out/production/shared/shared.js',
    'out/production/client/client.js',
}

server_scripts {
    'server.lua',

    'out/production/server/lib/kotlin.js',
    'out/production/shared/shared.js',
    'out/production/server/server.js',
}

files {}
