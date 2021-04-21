module.exports = {
  devServer: {
      port: 80,
      proxy: {
          '/api': {
              target: 'http://localhost:9999',
              changeOrigin: true,
              ws: true,
              pathRewrite: {
                '^/api': ''
              }
          },

      },
      disableHostCheck: true,
  },
  transpileDependencies: ['vuetify'],
  productionSourceMap: false,

  pluginOptions: {
    i18n: {
      locale: 'en',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: false,
    },
  },
}
