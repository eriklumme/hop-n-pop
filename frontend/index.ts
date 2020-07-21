import {Flow} from '@vaadin/flow-frontend/Flow';
import {Router} from '@vaadin/router';

import './global-styles';

const {serverSideRoutes} = new Flow({
    imports: () => import('../target/frontend/generated-flow-imports'),
});

const routes = [
    // for client-side, place routes below (more info https://vaadin.com/docs/v15/flow/typescript/creating-routes.html)
    {
        path: '',
        component: 'main-view',
        action: async () => {
            await import ('./views/main/main-view');
        }
    },
    {
        path: 'tilemap',
        component: 'tilemap-generator',
        action: async() => {
            await import ('./views/main/tilemap-generator');
        }
    },
    ...serverSideRoutes
];

export const router = new Router(document.querySelector('#outlet'));
router.setRoutes(routes);
