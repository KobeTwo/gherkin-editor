var data =[
    {
        name: '/',
        children: [
            { name: 'hello1' },
            { name: 'wat1' },
            {
                name: 'child folder2',
                children: [
                    {
                        name: 'child folder3',
                        children: [
                            { name: 'hello3' },
                            { name: 'wat3' }
                        ]
                    },
                    { name: 'hello4' },
                    { name: 'wat4' },
                    {
                        name: 'child folder5',
                        children: [
                            { name: 'hello5' },
                            { name: 'wat5' }
                        ]
                    }
                ]
            }
        ]
    }
]

Vue.component('scenario-sidebar', {
    template: '#scenario-sidebar',
    data:  function () {
        return {
            roots: data
        }
    },
    methods: {
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
        isFolder: function () {
            return this.model.children &&
                this.model.children.length
        },
        isSelected: function () {
            return this.model.name === this.$root.selectedTreeElement
        }
    },
    methods: {
        toggle: function () {
            if (this.isFolder) {
                this.open = !this.open
            }
        },
        select: function () {
            this.$root.selectedTreeElement = this.model.name
        }
    }
})


