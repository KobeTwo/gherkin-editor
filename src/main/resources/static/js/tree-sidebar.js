var getTreeStructureURL = '/rest/api/treeStructure'
var getScenariosURL = '/rest/api/scenario/search/findChildrenRecursive'


Vue.component('tree-sidebar', {
    template: '#tree-sidebar',
    data: function () {
        return {
            roots: null
        }
    },
    computed: {
        selectedTreeItem: function () {
            return this.$root.selectedTreeElement;
        }
    },
    created: function () {
        this.fetchTreeStructure(this.$root.currentProject.id)
    },
    watch: {
        selectedTreeItem: function (val) {
            switch (this.selectedTreeItem.type) {
                case 'FOLDER':
                    url = '/' + this.$root.currentProject.id + /folder/
                    if (this.selectedTreeItem.model.id !== 'folderRoot') {
                        url = url + this.selectedTreeItem.model.id
                    }
                    history.pushState({}, '', url);
                    break
                case 'FEATURE':
                    url = '/' + this.$root.currentProject.id + /feature/
                    history.pushState({}, '', url);
                    break
            }
        }
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
                    rootFolder = {
                        model: {id: 'folderRoot', path: '', fileName: '/'},
                        children: result,
                        type: 'FOLDER'
                    }
                    rootFolderList = [rootFolder]
                    if (selectedTreeItem) {
                        self.$root.selectedTreeElement = selectedTreeItem
                    } else {
                        self.$root.selectedTreeElement = rootFolder
                    }
                    self.roots = rootFolderList
                },

            });
        }
    }
})
// define the item component
Vue.component('tree-sidebar-item', {
    name: 'tree-sidebar-item',
    template: '#tree-sidebar-item',
    props: {
        item: Object,
    },
    data: function () {
        return {
            open: false
        }
    },
    created: function () {
        if (this.isSelected) {
            this.$parent.setOpen()
        }
    },
    computed: {
        isSelected: function () {
            return this.item.model.id === this.$root.selectedTreeElement.model.id
        }
    },
    watch: {
        open: function (val) {
            if (this.open) {
                this.$parent.setOpen()
            }
        },
        isSelected: function (val) {
            if (this.isSelected) {
                this.$parent.setOpen()
            }
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
        },
        setOpen: function () {
            this.open = true;
            this.$parent.setOpen()
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
        this.fetchScenarios(this.$root.currentProject.id, this.selectedTreeItem.model.path + +this.selectedTreeItem.model.fileName)
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


