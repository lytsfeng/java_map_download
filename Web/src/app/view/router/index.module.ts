import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NgZorroAntdModule } from 'src/app/ng-zorro-antd.module';
import { IndexPage } from '../page/index.page';
import { IndexRoutingModule } from './index.routing.module';

@NgModule({
    imports: [
        CommonModule,
        IndexRoutingModule,
        NgZorroAntdModule,
    ],
    declarations: [
        IndexPage
    ],
    providers: []
})
export class IndexModule {
}