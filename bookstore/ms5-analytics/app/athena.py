import os, time, boto3
from fastapi import HTTPException

AWS_REGION      = os.getenv("AWS_DEFAULT_REGION", "us-east-1")
S3_RESULTS      = os.getenv("ATHENA_S3_RESULTS",  "s3://bookstore-datalake-2026sl/athena-results/")
ATHENA_DB       = os.getenv("ATHENA_DB",           "bookstore_catalog")
ATHENA_WORKGROUP = os.getenv("ATHENA_WORKGROUP",   "primary")

def get_athena():
    return boto3.client(
        "athena",
        region_name=AWS_REGION,
        aws_access_key_id     = os.getenv("AWS_ACCESS_KEY_ID"),
        aws_secret_access_key = os.getenv("AWS_SECRET_ACCESS_KEY"),
        aws_session_token     = os.getenv("AWS_SESSION_TOKEN"),
    )

def run_query(sql: str) -> list[dict]:
    """Ejecuta una query en Athena y devuelve los resultados como lista de dicts."""
    athena = get_athena()

    resp = athena.start_query_execution(
        QueryString=sql,
        QueryExecutionContext={"Database": ATHENA_DB},
        ResultConfiguration={"OutputLocation": S3_RESULTS},
        WorkGroup=ATHENA_WORKGROUP,
    )
    qid = resp["QueryExecutionId"]

    # Esperar resultado (máx 60 s)
    for _ in range(30):
        time.sleep(2)
        state = athena.get_query_execution(QueryExecutionId=qid)
        status = state["QueryExecution"]["Status"]["State"]
        if status == "SUCCEEDED":
            break
        if status in ("FAILED", "CANCELLED"):
            reason = state["QueryExecution"]["Status"].get("StateChangeReason", "")
            raise HTTPException(status_code=500, detail=f"Athena error: {reason}")

    result = athena.get_query_results(QueryExecutionId=qid)
    rows   = result["ResultSet"]["Rows"]
    if not rows:
        return []

    headers = [c["VarCharValue"] for c in rows[0]["Data"]]
    return [
        {headers[i]: (col.get("VarCharValue", "") if col else "")
         for i, col in enumerate(row["Data"])}
        for row in rows[1:]
    ]
