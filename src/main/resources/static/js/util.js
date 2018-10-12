var Utils = (function () {

    return {
        getConcatenatedPathByPathAndFileName: function (path, fileName) {
            concatenatedPath = null;
            if (path && !path.endsWith('/')) {
                path = path + '/'
            }
            if (fileName) {
                if (!fileName.endsWith('/')) {
                    fileName = fileName + '/'
                }
            } else {
                fileName = ''
            }

            return path + fileName;

        },
        getConcatenatedPath: function (pathItem) {
            return this.getConcatenatedPathByPathAndFileName(pathItem.path, pathItem.fileName)
        },
        getUrlForFeature: function (project, feature) {
            return "/" + project.id + "/feature/" + feature.id
        },
        getUrlForScenario: function (project, scenario) {
            return "/" + project.id + "/scenario/" + scenario.id
        },
        getFileNameWithoutSuffix: function (folderItem) {
            return folderItem.fileName.replace('.feature', '')
        }
    }

}())