from fastapi import FastAPI, Header, HTTPException
from pydantic import BaseModel
from transformers import pipeline
import time
import os

app = FastAPI()

summarizer = pipeline("summarization", model="IrisWiris/email-summarizer")

API_KEY = os.getenv("SUMMARIZER_SECRET_KEY", "")

class TextRequest(BaseModel):
    text: str
    max_length: int = 80
    min_length: int = 20

@app.post("/summarize")
def summarize(req: TextRequest, x_api_key: str = Header(...)):
    if x_api_key != API_KEY:
        raise HTTPException(status_code=403, detail="Forbidden")
        
    time.sleep(30)

    input_text = "summarize: " + req.text
    result = summarizer(
        input_text,
        max_length=req.max_length,
        min_length=req.min_length,
        do_sample=False
    )

    return {"summary": result[0]["summary_text"]}

