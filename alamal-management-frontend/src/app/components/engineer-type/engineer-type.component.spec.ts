import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EngineerTypeComponent } from './engineer-type.component';

describe('EngineerTypeComponent', () => {
  let component: EngineerTypeComponent;
  let fixture: ComponentFixture<EngineerTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EngineerTypeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EngineerTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
