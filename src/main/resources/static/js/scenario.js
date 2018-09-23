var data =[{
        name: 'My Tree',
        children: [
            { name: 'hello' },
            { name: 'wat' },
            {
                name: 'child folder',
                children: [
                    {
                        name: 'child folder',
                        children: [
                            { name: 'hello' },
                            { name: 'wat' }
                        ]
                    },
                    { name: 'hello' },
                    { name: 'wat' },
                    {
                        name: 'child folder',
                        children: [
                            { name: 'hello' },
                            { name: 'wat' }
                        ]
                    }
                ]
            }
        ]
    },
    {
        name: 'My Tree',
        children: [
            { name: 'hello' },
            { name: 'wat' },
            {
                name: 'child folder',
                children: [
                    {
                        name: 'child folder',
                        children: [
                            { name: 'hello' },
                            { name: 'wat' }
                        ]
                    },
                    { name: 'hello' },
                    { name: 'wat' },
                    {
                        name: 'child folder',
                        children: [
                            { name: 'hello' },
                            { name: 'wat' }
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
        model: Object
    },
    data: function () {
        return {
            open: false,
            selected: false
        }
    },
    computed: {
        isFolder: function () {
            return this.model.children &&
                this.model.children.length
        },
        isSelected: function () {
            return this.selected
        }
    },
    methods: {
        toggle: function () {
            if (this.isFolder) {
                this.open = !this.open
            }
        },
        select: function () {
            this.selected = true
            console.log("selected")
        }
    }
})


