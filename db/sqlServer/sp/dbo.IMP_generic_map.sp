
/****** Object:  StoredProcedure [dbo].[IMP_generic_map]    Script Date: 18/12/2019 12:38:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	CREATE PROC [dbo].[IMP_generic_map]
	 @tx_action_name		varchar(32)		= NULL	OUTPUT

	, @is_active			int						= 1
	, @id_env_key			int						= NULL	OUTPUT
	, @id_user_mod_key 		int						= NULL	OUTPUT
	, @dtt_mod				datetime				= NULL	OUTPUT

	, @id_state_key			int						= NULL	OUTPUT
	, @tx_state_name		varchar(64)	= NULL	OUTPUT
	, @id_action_key		int						= NULL  OUTPUT

	, @id_event_key			int						= NULL	OUTPUT
	, @id_event_map1_key	int						= NULL	OUTPUT
	, @id_event_map2_key	int						= NULL	OUTPUT
	, @id_event_map3_key	int						= NULL	OUTPUT
	, @id_event_map4_key	int						= NULL	OUTPUT

	, @dtt_valid_from		datetime				= NULL
	, @dtt_valid_to			datetime				= NULL
	, @dtt_as_at			datetime				= NULL
	, @dtt_last_refresh		datetime				= NULL
	 , @id_from_key 			int 					= NULL
	 , @id_from_key_ver 		int 					= NULL
	 , @tx_from_type_name		varchar(64)	= NULL
	 , @tx_to_type_name			varchar(64)	= NULL	OUTPUT
	 , @id_request_key 			int
	 , @is_sel_data			int						= NULL
	, @tx_log_level			varchar(32)			= NULL
	, @id_log_level			int						= NULL
	, @is_record_time		bit						= NULL
	, @is_print_msg			bit						= NULL
	, @is_persist_msg		bit						= NULL
	, @tx_json_log_msg		varchar(MAX)	= NULL		OUTPUT
AS
BEGIN
	IF ( ((@id_user_mod_key IS NULL) OR (@id_user_mod_key = -2147483648)))
	BEGIN
		SELECT	@id_user_mod_key = ISNULL(id_user_mod_key, 0)
		FROM	T_USER
		WHERE	tx_login_name = SUSER_NAME()
	END

	IF ( ((@id_user_mod_key IS NULL) OR (@id_user_mod_key = -2147483648)))
	BEGIN
		PRINT 'defaulting USER to nazdaq_prod'
		SELECT @id_user_mod_key = id_user_mod_key
		FROM	T_USER
		WHERE	tx_login_name = 'nazdaq_prod'
	END
	-- GLOBAL VARS --
	DECLARE	  @g_tx_action_name_orig	varchar(32)
			, @g_tx_state_name_orig		varchar(64)

			, @g_id_env_key				int
			, @g_tx_env_name			varchar(256)

			, @g_ct_row					int
			, @g_id_return_status		int

			, @g_id_error_key			int
			, @g_tx_err_msg				varchar(1024)
			, @g_tx_err_msg_tmp			varchar(1024)
			, @g_is_outer_sp			bit

	DECLARE   @g_dt_sys_today			date
			, @g_dt_sys_prev_day		date
			, @g_dt_sys_next_day		date
			, @g_dt_sys_prev_biz_day	date
			, @g_dt_sys_next_biz_day	date

	

	--	DECLARE @g_in_tran		int
	DECLARE	@g_is_ext_txn	bit
	DECLARE @g_is_sp_txn	bit

	SELECT @g_is_ext_txn = 0, @g_is_sp_txn = 0

	DECLARE @g_tmp_int		int

	DECLARE   @g_dtt_log			datetime
	 		, @g_tx_tmp_log_msg		varchar(MAX)
	 		, @g_tx_log_msg			varchar(MAX)

		  	, @g_id_err_num			int
			, @g_id_err_sev			int
			, @g_id_err_state		int
			, @g_tx_sp_name			varchar(128)
			, @g_id_line_num		int
			, @g_id_log_level		int

			, @g_is_record_time		bit
			, @g_is_print_msg		bit
			, @g_is_persist_msg  	bit

			, @g_tx_json_log_msg	varchar(MAX)

	SELECT	@g_tx_json_log_msg = ''

	DECLARE @g_dtt_proc_start		datetime
	DECLARE @g_dtt_proc_end			datetime
	DECLARE @g_dtt_tot_elapsed		int

	DECLARE	@g_dtt_query_start		datetime
	DECLARE	@g_dtt_query_end		datetime
	DECLARE @g_dtt_query_elapsed	int

	DECLARE @g_tmp_tx_tot_time		varchar(255)
	DECLARE @g_tmp_tx_query_time	varchar(255)

	IF ( (@@NESTLEVEL = 1) )
	BEGIN
		IF ( ((@@OPTIONS & 2) != 2) AND (@@TRANCOUNT = 0) )
		BEGIN
			SELECT @g_is_ext_txn = 0
		END
		ELSE
		BEGIN
			SELECT @g_is_ext_txn = 1
		END
	END

	IF (((@tx_log_level IS NULL) OR (@tx_log_level = '?')) OR ((@is_persist_msg IS NULL) OR (@is_persist_msg = -2147483648)) OR ((@is_record_time IS NULL) OR (@is_record_time = -2147483648)))
	BEGIN
		EXEC @g_id_return_status = GET_sp_log_level @tx_sp_name='IMP_generic_map', @tx_log_level=@tx_log_level OUTPUT, @id_log_level=@id_log_level OUTPUT, @is_record_time=@is_record_time OUTPUT, @is_print_msg=@is_print_msg OUTPUT, @is_persist_msg=@is_persist_msg OUTPUT
	END

	SELECT @g_is_record_time = @is_record_time, @g_is_print_msg = @is_print_msg, @g_is_persist_msg	= @is_persist_msg, @g_id_log_level = @id_log_level

	SELECT @g_tx_err_msg = 'Entering[IMP_generic_map] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'IMP_generic_map.sp', @g_id_line_num = 153

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	IF (((@id_env_key IS NULL) OR (@id_env_key = -2147483648)) AND @tx_action_name != 'LOGIN')
	BEGIN
		EXEC @g_id_return_status = GET_environment @id_env_key = @id_env_key OUTPUT, @tx_env_name	= @g_tx_env_name OUTPUT, @id_user_key = @id_user_mod_key

		SELECT @g_tx_err_msg = 'Error calling SP -> [GET_ENVIRONMENT] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END
	END

	SELECT @g_id_env_key = @id_env_key

	IF (((@dtt_mod IS NULL) OR (@dtt_mod = '01/01/1970')))
	BEGIN
		SELECT @dtt_mod = GETDATE()
	END

	IF (@tx_action_name != 'LOGIN')
	BEGIN
		IF (((@id_user_mod_key IS NULL) OR (@id_user_mod_key = -2147483648)))
		BEGIN
			RAISERROR ('Error:Int [@id_user_mod_key] should not be NULL', 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30011 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30011
		END
	END

	-- X_SET_NULL_ACTION

	SELECT  @g_tx_action_name_orig = @tx_action_name

	SET NOCOUNT ON

	---get type id
	DECLARE   @id_from_type_key int
			, @id_to_type_key 	int

	IF ( ((@id_from_type_key IS NULL) OR (@id_from_type_key = -2147483648)) AND ((@tx_from_type_name IS NULL) OR (@tx_from_type_name = '?')) )
	BEGIN
		SELECT @g_tx_err_msg = 'Must pass in either _type_id_ or @tx_from_type_name'
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (99999 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 99999
	END
	ELSE
	BEGIN
		SELECT	@id_from_type_key	= id_type_key
			  ,	@tx_from_type_name	= tx_type_name		
		FROM	T_TYPE 
		WHERE	tx_type_category				= ISNULL(NULL	,	tx_type_category)
		AND		tx_type_name					= ISNULL(@tx_from_type_name		,	tx_type_name)
		AND		id_type_key						= ISNULL(@id_from_type_key			,	id_type_key)	
		AND		is_active		= ISNULL(@is_active		, is_active)
			--	AND		id_env_key		= ISNULL(@id_env_key	, id_env_key)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error selecting data ' + ' from T_TYPE ' + '@tx_from_type_name [' + CONVERT(varchar, @tx_from_type_name) + '] '
		IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30008 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30008
		END
	END
	IF ( ((@id_to_type_key IS NULL) OR (@id_to_type_key = -2147483648)) AND ((@tx_to_type_name IS NULL) OR (@tx_to_type_name = '?')) )
	BEGIN
		SELECT @g_tx_err_msg = 'Must pass in either _type_id_ or @tx_to_type_name'
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (99999 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 99999
	END
	ELSE
	BEGIN
		SELECT	@id_to_type_key	= id_type_key
			  ,	@tx_to_type_name	= tx_type_name		
		FROM	T_TYPE 
		WHERE	tx_type_category				= ISNULL(NULL	,	tx_type_category)
		AND		tx_type_name					= ISNULL(@tx_to_type_name		,	tx_type_name)
		AND		id_type_key						= ISNULL(@id_to_type_key			,	id_type_key)	
		AND		is_active		= ISNULL(@is_active		, is_active)
			--	AND		id_env_key		= ISNULL(@id_env_key	, id_env_key)

		SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
		SELECT @g_tx_err_msg = 'Error selecting data ' + ' from T_TYPE ' + '@tx_to_type_name [' + CONVERT(varchar, @tx_to_type_name) + '] '
		IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30008 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30008
		END
	END

	IF (((@id_from_type_key IS NULL) OR (@id_from_type_key = -2147483648)))
	BEGIN
		RAISERROR ('Error:Int [@id_from_type_key] should not be NULL', 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30011 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30011
	END
	IF (((@id_to_type_key IS NULL) OR (@id_to_type_key = -2147483648)))
	BEGIN
		RAISERROR ('Error:Int [@id_to_type_key] should not be NULL', 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30011 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30011
	END

	---get all mapped entry to a tmp table

	UPDATE STAGE.T_GENERIC_MAP
	SET  	id_from_key_ver 		= @id_from_key_ver
	WHERE 	id_from_key 			= @id_from_key
	AND 	tx_from_type_name 		= @tx_from_type_name
	AND 	tx_to_type_name 		= @tx_to_type_name
	AND 	id_request_key 			= @id_request_key

	SELECT 	  id_generic_map_key 
			, is_active 
			, id_from_key 
			, id_from_key_ver 
			, id_to_key 
			, id_to_key_ver
			, id_from_type_key
			, id_to_type_key
	INTO 	#TMP_GENERIC_MAP
	FROM 	T_GENERIC_MAP WITH (NOLOCK)
	WHERE  	id_from_key 		= @id_from_key
	--AND 	id_from_key_ver 	= @id_from_key_ver
	AND 	id_from_type_key 	= @id_from_type_key
	AND 	id_to_type_key 		= @id_to_type_key


	ALTER TABLE #TMP_GENERIC_MAP add tx_status varchar(32)

	---update both status to map/unmap

	UPDATE 	TM
	SET 	TM.tx_status = 'EXIST'
	FROM 	#TMP_GENERIC_MAP TM
	JOIN 	STAGE.T_GENERIC_MAP SM 	ON SM.id_from_key 		= TM.id_from_key
									AND SM.id_to_key 		= TM.id_to_key
	WHERE 	SM.id_from_key 			= @id_from_key
	--AND 	SM.id_from_key_ver 		= @id_from_key_ver
	AND 	SM.tx_from_type_name 	= @tx_from_type_name
	AND 	SM.tx_to_type_name 		= @tx_to_type_name
	AND 	SM.id_request_key 		= @id_request_key

	UPDATE 	SM
	SET 	  SM.tx_status 				= 'EXIST'
			, SM.id_generic_map_key_res = TM.id_generic_map_key
	FROM 	#TMP_GENERIC_MAP TM
	JOIN 	STAGE.T_GENERIC_MAP SM 	ON SM.id_from_key 		= TM.id_from_key
									AND SM.id_to_key 		= TM.id_to_key
	WHERE 	SM.id_from_key 			= @id_from_key
	--AND 	SM.id_from_key_ver 		= @id_from_key_ver
	AND 	SM.tx_from_type_name 	= @tx_from_type_name
	AND 	SM.tx_to_type_name 		= @tx_to_type_name
	AND 	SM.id_request_key 		= @id_request_key

	/*UPDATE 	SM
	SET 	  SM.tx_status 				= 'UPDATE'
			, SM.id_generic_map_key_res = TM.id_generic_map_key
	FROM 	#TMP_GENERIC_MAP TM
	JOIN 	STAGE.T_GENERIC_MAP SM 	ON SM.id_generic_map_key_res = TM.id_generic_map_key
	WHERE 	SM.id_from_key 			= @id_from_key
	AND 	SM.id_from_key_ver 		!= @id_from_key_ver
	AND 	SM.tx_from_type_name 	= @tx_from_type_name
	AND 	SM.tx_to_type_name 		= @tx_to_type_name*/
	
	UPDATE 	SM
	SET 	  SM.tx_status 				= 'UPDATE'
			, SM.id_generic_map_key_res = TM.id_generic_map_key
	FROM 	#TMP_GENERIC_MAP TM
	JOIN 	STAGE.T_GENERIC_MAP SM 	ON SM.id_generic_map_key_res = TM.id_generic_map_key
	WHERE 	SM.id_from_key 			= @id_from_key
	--AND 	SM.id_from_key_ver 		= @id_from_key_ver
	AND 	SM.tx_from_type_name 	= @tx_from_type_name
	AND 	SM.tx_to_type_name 		= @tx_to_type_name
	AND 	TM.is_active 			= 0
	AND 	SM.id_request_key 		= @id_request_key

	CREATE TABLE #T_GENERIC_MAP_KEYS
	(	
		  id 					int IDENTITY(0 ,1)
		, id_generic_map_key 	int
		, id_to_key 			int 
	)
	INSERT INTO #T_GENERIC_MAP_KEYS
	SELECT 	0 , id_to_key
	FROM 	STAGE.T_GENERIC_MAP SM
	WHERE 	SM.tx_status IS NULL
	AND 	SM.id_from_key 			= @id_from_key
	--AND 	SM.id_from_key_ver 		= @id_from_key_ver
	AND 	SM.tx_from_type_name 	= @tx_from_type_name
	AND 	SM.tx_to_type_name 		= @tx_to_type_name
	AND 	SM.id_request_key 		= @id_request_key

	SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
	SELECT @g_tx_log_msg = 'MAP INFO NEW                 : ' + CONVERT(varchar, @g_ct_row)
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'IMP_generic_map.sp', @g_id_line_num = 662

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END



	DECLARE @l_id_generic_map_key int

	-- GET System Keys
	EXEC @g_id_return_status = GET_system_key @id_env_key	= @g_id_env_key, @tx_key_name = 'id_generic_map_key', @id_key_value	= @l_id_generic_map_key OUTPUT, @num_keys = @g_ct_row

	SELECT @g_tx_err_msg = 'Error generating key for id_generic_map_key'
	IF (@g_id_return_status != 0)
	BEGIN
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30003 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30003
	END

	UPDATE 	#T_GENERIC_MAP_KEYS
	SET		id_generic_map_key = @l_id_generic_map_key + id

	UPDATE 	SM 
	SET   	id_generic_map_key_res = TMP.id_generic_map_key
	FROM 	STAGE.T_GENERIC_MAP SM
	JOIN 	#T_GENERIC_MAP_KEYS TMP  ON TMP.id_to_key 		= SM.id_to_key
	WHERE	SM.tx_status  IS NULL
	AND 	SM.id_from_key 			= @id_from_key
	--AND 	SM.id_from_key_ver 		= @id_from_key_ver
	AND 	SM.tx_from_type_name 	= @tx_from_type_name
	AND 	SM.tx_to_type_name 		= @tx_to_type_name
	AND 	SM.id_request_key 		= @id_request_key


	IF ( ((@id_event_key IS NULL) OR (@id_event_key = -2147483648)) )
	BEGIN
		SELECT @g_is_outer_sp	= 1, @id_event_map1_key = 0, @id_event_map2_key = 0, @id_event_map3_key = 0, @id_event_map4_key = 0

		EXEC @g_id_return_status = GET_system_key @id_env_key	= @g_id_env_key, @tx_key_name = 'id_event_key', @id_key_value	= @id_event_key OUTPUT, @num_keys = 1
	
		SELECT @g_tx_err_msg = 'Error generating key for id_event_key'
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30003 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30003
		END
	END


	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'IMP_generic_map.sp', @g_id_line_num = 831

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('--- BEGIN MAIN TXN ---', '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='--- BEGIN MAIN TXN ---', @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	BEGIN TRY
		IF (NOT (@g_is_ext_txn = 1 OR @@trancount > 0) )
		BEGIN
			SELECT @g_is_sp_txn = 1
			BEGIN TRANSACTION
		END

			SELECT @g_tx_log_msg = 'BEGIN TX'
			SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'IMP_generic_map.sp', @g_id_line_num = 900
		
			-- TODO_H : [Naz] Move to Function
		
			IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
			BEGIN
				IF (2 >= @g_id_log_level)
				BEGIN
					IF (@g_is_print_msg = 1)
					BEGIN
						SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
						PRINT  @g_tx_tmp_log_msg
					END
		
					IF ( (@g_is_persist_msg = 1) )
					BEGIN
						EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
															   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
															   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
					
						IF (@g_id_return_status != 0)
						BEGIN
							RAISERROR ('Error calling ACT_msg_log', 16, 1)
							IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
							IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
							BEGIN
								-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
						
								IF (30008 = 0)
								BEGIN
									--RAISERROR (@tx_json_log_msg, 10, 1)
									EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
						
								END
								ELSE
								BEGIN
									--RAISERROR (@tx_json_log_msg, 16, 1)
									EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
								END
							END
						
							RETURN 30008
						END
					END
				END
			END

	---insert new if any

	INSERT INTO T_GENERIC_MAP
	(
		  id_generic_map_key
		, id_generic_map_ver
		, -- _VERSION
		 is_active
		, id_env_key
		, id_user_mod_key
		, dtt_mod
		, id_event_key
		, id_state_key
		, id_action_key
		, dtt_valid_from
		, dtt_valid_to
		, id_from_key
		, id_from_key_ver
		, id_from_type_key
		, id_to_key
		, id_to_key_ver
		, id_to_type_key
		, is_primary
		, ct_sort_order
		, tx_map_desc
	)
	SELECT    id_generic_map_key_res
			, 0
			, --  ISNULL(@_VERSION			, 0)
			  ISNULL(@is_active			, 1)
			, ISNULL(@id_env_key		, 0)
		   	, ISNULL(@id_user_mod_key	, 0)
			, ISNULL(@dtt_mod			, GETDATE())
			, ISNULL(@id_event_key		, 0)
			, ISNULL(@id_state_key		, 0)
			, ISNULL(@id_action_key		, 0)
			, ISNULL(@dtt_valid_from	, '01/01/1970')
			, ISNULL(@dtt_valid_to		, '01/01/1970')
			, id_from_key
			, id_from_key_ver
			, @id_from_type_key
			, id_to_key
			, id_to_key_ver
			, @id_to_type_key
			, ISNULL(is_primary , 0)
			, ISNULL(ct_sort_order , 0)
			, '?'
	FROM 	STAGE.T_GENERIC_MAP
	WHERE 	tx_status IS NULL
	AND 	id_from_key 		= @id_from_key
	--AND 	id_from_key_ver 	= @id_from_key_ver
	AND 	tx_from_type_name 	= @tx_from_type_name
	AND 	tx_to_type_name 	= @tx_to_type_name
	AND 	id_request_key 		= @id_request_key

	---update if any

	UPDATE 	T 
	SET 	is_active = 1
			, dtt_mod = GETDATE()
			, id_generic_map_ver = id_generic_map_ver + 1
	FROM 	T_GENERIC_MAP  T
	JOIN 	STAGE.T_GENERIC_MAP ST ON ST.id_generic_map_key_res = T.id_generic_map_key
	WHERE  	ST.tx_status 			= 'UPDATE'
	AND 	ST.id_from_key 			= @id_from_key
	--AND 	ST.id_from_key_ver 		= @id_from_key_ver
	AND 	ST.tx_from_type_name 	= @tx_from_type_name
	AND 	ST.tx_to_type_name 		= @tx_to_type_name
	AND 	ST.id_request_key 		= @id_request_key

	---delete if any

	UPDATE 	T 
	SET 	is_active = 0
			, dtt_mod = GETDATE()
			, id_generic_map_ver = id_generic_map_ver + 1
	FROM 	T_GENERIC_MAP  T
	JOIN 	#TMP_GENERIC_MAP ST ON ST.id_generic_map_key = T.id_generic_map_key
	WHERE  	ST.tx_status  IS NULL
	


	DELETE 
	FROM 	STAGE.T_GENERIC_MAP
	WHERE 	id_from_key 		= @id_from_key
	--AND 	id_from_key_ver 	= @id_from_key_ver
	AND 	tx_from_type_name 	= @tx_from_type_name
	AND 	tx_to_type_name 	= @tx_to_type_name
	AND 	id_request_key 		= @id_request_key

	IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		COMMIT TRANSACTION
		
		SELECT @tx_json_log_msg = ''
	END
	END TRY
	BEGIN CATCH

		SELECT	  @g_dtt_log = GETDATE(), @g_id_err_num = ERROR_NUMBER(), @g_id_err_sev = ERROR_SEVERITY(), @g_id_err_state = ERROR_STATE()
				, @g_tx_sp_name = 'IMP_generic_map.sp', @g_id_line_num = ERROR_LINE(), @g_tx_err_msg = ISNULL(NULL, '') + ' ' + ERROR_MESSAGE()
	
		IF (@g_is_print_msg = 1)
		BEGIN
			SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'ERROR' + ' | ' + '?' + ' | [' + CONVERT(varchar, @g_id_err_num) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
			PRINT  @g_tx_tmp_log_msg
		END
	
		-- X_PERSIST_MSG(@g_dtt_log, '?', 'ERROR', 4, @g_tx_sp_name, @g_id_line_num, @g_id_err_num, @g_tx_err_msg) 

		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (@g_id_err_num = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN @g_id_err_num

	END CATCH
	
	IF ( (@g_is_record_time = 1) )
		SELECT @g_dtt_proc_end = GETDATE()
	IF ( (@g_is_record_time = 1) )
		SELECT @g_dtt_tot_elapsed  = datediff(ss, @g_dtt_proc_start, @g_dtt_proc_end)

	SELECT @g_tx_err_msg = 'Exiting[IMP_generic_map] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'IMP_generic_map.sp', @g_id_line_num = 1114

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
	BEGIN
		-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'

		IF (0 = 0)
		BEGIN
			--RAISERROR (@tx_json_log_msg, 10, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10

		END
		ELSE
		BEGIN
			--RAISERROR (@tx_json_log_msg, 16, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
		END
	END

	RETURN 0

	RETURN 0
END
