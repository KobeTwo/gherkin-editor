var vueBody = new Vue({

    el: '#vue-homepage',

    data: {
        allprojects: null
    },
    created: function () {
        this.fetchProjects()
        if (!this.selectedTreeElement) {
            this.selectedTreeElement = {model: {path: '/', fileName: ''}}
        }
        if (!this.highlightedTreeElement) {
            this.highlightedTreeElement = {model: {path: '/', fileName: ''}}
        }
    },
    methods: {
        fetchProjects: function () {
            var self = this
            $.ajax({
                url: getProjectURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.allprojects = result._embedded.project
                },

            });
        }
    }
})