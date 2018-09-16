resource_manifest_version '05cfa83c-a124-4cfa-a768-c24a5811d8f9'

resource_type 'data' {
    name = 'test'
}

files {
    'resources/radio/index.html'
}

ui_page "resources/radio/index.html"

client_scripts {
    'client.lua',

    'Base64.min.js',

    'out/production/client/lib/kotlin.js',
    'out/production/shared/shared.js',
    'out/production/client/client.js',
}

server_scripts {
    'server.lua',

    'Base64.min.js',

    'out/production/server/lib/kotlin.js',
    'out/production/shared/shared.js',
    'out/production/server/server.js',
}

