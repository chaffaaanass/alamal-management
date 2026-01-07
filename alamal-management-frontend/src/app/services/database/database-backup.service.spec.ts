import { TestBed } from '@angular/core/testing';

import { DatabaseBackupService } from './database-backup.service';

describe('DatabaseBackupService', () => {
  let service: DatabaseBackupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DatabaseBackupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
