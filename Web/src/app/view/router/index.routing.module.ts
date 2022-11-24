import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { IndexPage } from '../page/index.page';

const routes: Routes = [
    { path: '', redirectTo: 'map', pathMatch: 'full' },
    {
        path: '', component: IndexPage, children: [
            { // 地图页面
                path: 'map',
                loadChildren: () => import('./map/map.module').then(mod => mod.MapModule)
            },
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class IndexRoutingModule {
}
