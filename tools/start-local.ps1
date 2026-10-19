param([switch]$Seed, [string]$JavaHome = 'D:\Program Files\Java\jdk1.8.0_152')
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
Set-Location -LiteralPath $projectRoot
$config = Get-Content -LiteralPath "$projectRoot\private\local-config.json" -Raw | ConvertFrom-Json
foreach ($key in @('DB_URL','DB_USERNAME','DB_PASSWORD','JWT_SECRET','DEMO_ADMIN_PASSWORD','DEMO_USER_PASSWORD')) {
    [Environment]::SetEnvironmentVariable($key, $config.$key, 'Process')
}
$env:UPLOAD_DIR = "$projectRoot\private\uploads"
$env:SEED_DEMO = if ($Seed) { 'true' } else { 'false' }
$env:SEED_CONFIRM = if ($Seed) { 'EMPTY_DEMO_DATABASE' } else { '' }
New-Item -ItemType Directory -Path "$projectRoot\private\runtime" -Force | Out-Null
Copy-Item -LiteralPath "$projectRoot\mySpringboot\target\mySpringboot-0.0.1-SNAPSHOT.jar" -Destination "$projectRoot\private\runtime\app.jar" -Force
$backend = Start-Process -FilePath "$JavaHome\bin\java.exe" -ArgumentList @('-jar', 'private/runtime/app.jar') -WorkingDirectory $projectRoot -WindowStyle Hidden -RedirectStandardOutput "$projectRoot\private\backend.local.log" -RedirectStandardError "$projectRoot\private\backend.local.err" -PassThru
$pythonExe = (Get-Command python.exe).Source
$gateway = Start-Process -FilePath $pythonExe -ArgumentList 'tools/local_gateway.py' -WorkingDirectory $projectRoot -WindowStyle Hidden -RedirectStandardOutput "$projectRoot\private\gateway.local.log" -RedirectStandardError "$projectRoot\private\gateway.local.err" -PassThru
@{ backend = $backend.Id; gateway = $gateway.Id } | ConvertTo-Json | Set-Content -LiteralPath "$projectRoot\private\local-pids.json"
Write-Output 'Local demo: http://127.0.0.1:4173 (database must already listen on 5237)'
Write-Output 'Credentials: private/local-config.json (keep private).'
