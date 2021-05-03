import {Component, OnDestroy, OnInit} from '@angular/core';
import {RxStompService} from '@stomp/ng2-stompjs';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit, OnDestroy {
  public receivedMessages: any[] = [];
  private topicSubscription: Subscription;

  constructor(private rxStompService: RxStompService) {
  }

  onSendMessage(): void {
    const message = {
      from: 'foo',
      text: `Message generated at ${new Date()}`
    };

    this.rxStompService.publish({destination: '/xxx', body: JSON.stringify(message)});
  }

  ngOnInit(): void {
    this.rxStompService.watch('/topic/messages').subscribe(message => {
      const msg = JSON.parse(message.body);
      console.log(msg);
      this.receivedMessages.push(msg);
    });
  }

  ngOnDestroy(): void {
    this.topicSubscription.unsubscribe();
  }
}
