import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgZorroAntdModule } from 'src/app/ng-zorro-antd.module';
import { MapPage } from '../../page/map/map.page';
import { MapRoutingModule } from './map.routing.module';

@NgModule({
    imports: [
        CommonModule,
        MapRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        NgZorroAntdModule,
    ],
    declarations: [
        MapPage
    ],
    providers: []
})
export class MapModule {
}