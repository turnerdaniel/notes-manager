window.onload = function() {
  const HideInfoUrlPartsPlugin = () => {
    return {
      wrapComponents: {
        InfoUrl: () => () => null
      }
    }
  }

  // Begin Swagger UI call region
  const ui = SwaggerUIBundle({
    url: "./openapi.yaml",
    dom_id: '#swagger-ui',
    deepLinking: true,
    supportedSubmitMethods: [],
    presets: [
      SwaggerUIBundle.presets.apis
    ],
    plugins: [
      HideInfoUrlPartsPlugin
    ]
  });
  // End Swagger UI call region

  window.ui = ui;
};
