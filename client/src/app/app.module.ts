import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {myRxStompConfig} from './my-rx-stomp.config';
import {myRxStompRtcConfig} from './my-rx-stomp-rpc.config';
import {
  InjectableRxStompConfig,
  InjectableRxStompRPCConfig,
  RxStompRPCService,
  RxStompService,
  rxStompServiceFactory
} from '@stomp/ng2-stompjs';
import {MessagesComponent} from './messages/messages.component';

const heroServiceFactory = (config: InjectableRxStompRPCConfig, service: RxStompService) => {
  return new RxStompRPCService(service, config);
};

@NgModule({
  declarations: [
    AppComponent,
    MessagesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    {
      provide: InjectableRxStompConfig,
      useValue: myRxStompConfig,
    },
    {
      provide: InjectableRxStompRPCConfig,
      useValue: myRxStompRtcConfig,
    },
    {
      provide: RxStompService,
      // useFactory: rxStompServiceFactory,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig],
    }, {
      provide: RxStompRPCService,
      useFactory: heroServiceFactory,
      deps: [InjectableRxStompRPCConfig, RxStompService],
    }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
