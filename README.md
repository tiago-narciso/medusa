## Configuration

To run this project, you must have Docker and Docker Compose installed.

Update the dummy data in the `.env` file with your actual configuration. 

If you wish to change the storage location of the database, update the `volumes` section in the `docker-compose.yml` file.

> [!NOTE]
> The database is automatically initialized on the first run.

## Getting Started

### 1. Launch the Project

Start the containers using Docker Compose (you may need to run this command using sudo):

```bash
docker-compose up -d

```

Check everything is working by connecting to the Adminer front-end at `http://localhost:8080/`. The value of each fields are the one in `.env`, don't forget to select PostgreSQL as the system.

### 2. Load Data

This project includes a dataset for France. You can use the provided script `fill_db_from_jsonl.py` to populate the database. Since the database is persistent, you only need to run this step once.

#### Environment Setup

We recommend using a Python virtual environment:

```bash
cd scripts
python3 -m venv venv
source venv/bin/activate
pip install psycopg2-binary

```

#### Running the Import Script

The following command uses default local settings. Ensure you update these parameters if you are deploying to a production or remote environment.

```bash
python3 fill_db_from_jsonl.py \
    localhost \
    db \
    user \
    password \
    france_personalities.jsonl

```

| Parameter | Description |
| --- | --- |
| **Host** | Server address (use `localhost` for local runs) |
| **Database** | The name of your database |
| **Password** | Database password |
| **User** | Database username |
| **File Path** | Path to your `.jsonl` data file |
