var getTreeStructureURL = '/api/project1/folder/structure'

Vue.component('scenario-sidebar', {
    template: '#scenario-sidebar',
    data: function () {
        return {
            roots: null
        }
    },
    created: function () {
        this.fetchProjects()
    },
    methods: {
        fetchProjects: function () {
            var self = this
            $.ajax({
                url: getTreeStructureURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    self.roots = result
                },

            });
        },
        addChild: function () {
            alert("add child")
        }
    }
})
// define the item component
Vue.component('scenario-sidebar-treeitem', {
    name: 'scenario-sidebar-treeitem',
    template: '#scenario-sidebar-treeitem',
    props: {
        model: Object,
    },
    data: function () {
        return {
            open: false
        }
    },
    computed: {
        isSelected: function () {
            return this.model.model.id === this.$root.selectedTreeElement
        }
    },
    methods: {
        toggle: function () {
            if (this.model.type === 'FOLDER') {
                this.open = !this.open
            }
        },
        select: function () {
            this.$root.selectedTreeElement = this.model.model.id
        }
    }
})


