var getTreeStructureURL = '/api/treeStructure'
var getScenariosURL = '/api/scenario/search/findChildrenRecursive'


Vue.component('scenario-sidebar', {
    template: '#scenario-sidebar',
    data: function () {
        return {
            roots: null
        }
    },
    created: function () {
        this.fetchTreeStructure(this.$root.currentProject.id)
    },
    methods: {
        fetchTreeStructure: function (projectId) {
            var self = this
            $.ajax({
                url: getTreeStructureURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                data: {projectId: projectId},
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
        item: Object,
    },
    data: function () {
        return {
            open: false
        }
    },
    computed: {
        isSelected: function () {
            return this.item.model.id === this.$root.selectedTreeElement.model.id
        }
    },
    methods: {
        toggle: function () {
            if (this.item.type === 'FOLDER') {
                this.open = !this.open
            }
        },
        select: function () {
            this.$root.selectedTreeElement = this.item
        }
    }
})

Vue.component('scenario-list', {
    template: '#scenario-list',
    data: function () {
        return {
            scenarios: null
        }
    },
    computed: {
        selectedTreeItem: function () {
            return this.$root.selectedTreeElement;
        }
    },
    watch: {
        selectedTreeItem: function (val) {

            this.fetchScenarios(this.$root.currentProject.id, this.selectedTreeItem.model.path + this.selectedTreeItem.model.fileName)
        }
    },
    created: function () {
        this.fetchScenarios(this.$root.currentProject.id, this.selectedTreeItem.model.path)
    },
    methods: {
        fetchScenarios: function (projectId, path) {
            console.log('selected:' + projectId + '###' + path)
            var self = this
            $.ajax({
                url: getScenariosURL,
                type: 'GET',
                contentType: 'application/json',
                dataType: 'json',
                data: {projectId: projectId, path: path},
                success: function (result) {
                    self.scenarios = result._embedded.scenario
                },

            });
        }
    }
})


