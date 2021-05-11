import {Component, OnDestroy, OnInit} from '@angular/core';
import {RxStompRPCService, RxStompService} from '@stomp/ng2-stompjs';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit, OnDestroy {
  public receivedMessages: any[] = [];
  private interval: any;
  private i = 0;
  private j = 0;

  constructor(private rxStompService: RxStompService, private rxStompRPCService: RxStompRPCService) {
  }

  onSendMessage(): void {

    // execute rpc
    this.rxStompRPCService
      .rpc({destination: '/app/rpc', body: 'rpc-' + (this.j++)})
      .subscribe(result => {
        console.log('RPC', result.body);
      });
  }

  ngOnInit(): void {

    // receive message from server
    this.rxStompService.watch('/topic/to-client').subscribe(message => {
      console.log(message.body);
    });

    // send message to server
    this.interval = setInterval(() => {
      this.rxStompService.publish({
        destination: '/app/from-client',
        body: 'from-client-' + (this.i++)
      });
    }, 10000);
  }

  ngOnDestroy(): void {
    clearInterval(this.interval
    );
  }
}
