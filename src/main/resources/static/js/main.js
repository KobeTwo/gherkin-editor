var getProjectURL = '/api/project'
/**
 * home page project list
 */
Vue.component('homepage-projects', {
    template: '#homepage-projects',
    props: {
        allprojects: Array
    }
})

Vue.component('header-projects', {
    template: '#header-projects',
    props: {
        allprojects: Array
    }
})

var vueBody = new Vue({

    el: '#vue-support',

    data: {
        allprojects: null
    },
    created: function () {
        this.fetchProjects()
    },
    methods: {
        fetchProjects: function () {
            var xhr = new XMLHttpRequest()
            var self = this
            xhr.open('GET', getProjectURL)
            xhr.onload = function () {
                response = JSON.parse(xhr.responseText)
                self.allprojects = response._embedded.project
            }
            xhr.send()
        }
    }
})