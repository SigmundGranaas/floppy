package com.sigmundgranaas.core.service.job.api;

import com.sigmundgranaas.core.data.PdfJob;

public interface JobSubmitter {
    void submit(PdfJob job);
}