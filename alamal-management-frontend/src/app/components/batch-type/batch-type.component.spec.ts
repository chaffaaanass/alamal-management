import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BatchTypeComponent } from './batch-type.component';

describe('BatchTypeComponent', () => {
  let component: BatchTypeComponent;
  let fixture: ComponentFixture<BatchTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BatchTypeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BatchTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
