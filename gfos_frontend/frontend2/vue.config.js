module.exports = {
    devServer: {
        open: process.platform === 'darwin',
        host: '0.0.0.0',
        port: 80, // CHANGE YOUR PORT HERE!
        https: false,
        hotOnly: false,
    },
    transpileDependencies: [
        'vuetify'
    ]
}
