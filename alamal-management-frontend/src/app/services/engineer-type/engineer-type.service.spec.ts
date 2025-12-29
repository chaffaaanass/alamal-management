import { TestBed } from '@angular/core/testing';

import { EngineerTypeService } from './engineer-type.service';

describe('EngineerTypeService', () => {
  let service: EngineerTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EngineerTypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
