// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<style>
#debug-panel {
    height: 100%;
}
#debug-panel .row {
    display: flex;
    align-items: baseline;
    justify-content: space-between;
    width: 100%;
    margin-bottom: var(--lumo-space-s);
}
#debug-panel .row :first-child:not(:last-child) {
    margin-right: var(--lumo-space-m);
}
#debug-panel .row .full-width {
    flex-grow: 1;
}
#debug-panel .tab-content {
    display: none;
    padding: var(--lumo-space-m);
    flex-flow: column;
}
</style>
`;

document.head.appendChild($_documentContainer.content);
