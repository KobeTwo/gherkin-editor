var getProjectURL = '/api/project'
var postProjectURL = '/api/project'
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

Vue.component('create-project-modal', {
    template: '#create-project-modal',
    data:  function () {
        return {
            inputProject: {
                id: null
            },
            errorResult: null
        }
    },
    props: {
        allprojects: Array
    },
    methods: {
        processForm: function () {
            var self = this
            $.ajax({
                url: postProjectURL,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(this.inputProject),
                dataType: 'json',
                success: function(result){
                    self.errorResult = null
                    self.inputProject.id = '';
                    self.allprojects.push(result);
                    $(self.$refs["createProjectModal"]).modal('hide')
                },
                error: function(result){
                    self.errorResult = result.responseJSON
                }
            });
        }
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
            var self = this
            $.ajax({
                url: getProjectURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                success: function(result){
                    self.allprojects = result._embedded.project
                },

            });
        }
    }
})