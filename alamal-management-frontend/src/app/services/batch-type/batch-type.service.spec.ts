import { TestBed } from '@angular/core/testing';

import { BatchTypeService } from './batch-type.service';

describe('BatchTypeService', () => {
  let service: BatchTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BatchTypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
